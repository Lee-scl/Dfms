package com.docker.service;

import com.docker.constant.FileTypeEnum;
import com.docker.entity.FileInfo;
import com.docker.tasks.FileTree;
import com.docker.util.CacheUtil;
import com.docker.util.FileTypeUtil;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by CHEN on 2019/12/7.
 */
@Service
public class FileService {
    @Value("${fs.dir}")
    private String fileDir;

    @Value("${domain}")
    private String domain;

    private static final String SLASH = "/";


    /**
     * 获取全部文件、文件列表ok
     *
     * @param dir
     * @param accept
     * @param exts
     * @return Map
     */
    public Map list(String dir, String accept, String exts) {
        Map<String, Object> rs = new HashMap<>( );
        String[] mExts = null;
        if (exts != null && !exts.trim( ).isEmpty( )) {
            mExts = exts.split(",");
        }

        LinkedList<String> dlist = new LinkedList<>( );
        for (String s : dir.split("/")) {
            if (!s.equals(""))
                dlist.add(s);
        }

        FileInfo fi = FileTree.fileInfo.findChildFiles(dlist);
        ArrayList<FileInfo> flist = null;

        List<Map> dataList = new ArrayList<>( );

        if (fi != null) {
            flist = fi.getFiles( );
        }
        if (flist != null) {
            for (FileInfo f : flist
                    ) {
                if ("sm".equals(f.getFileName( ))) {
                    continue;
                }
                Map<String, Object> m = new HashMap<>(0);
                // 文件名称
                m.put("name", f.getFileName( ));
                // 修改时间
                m.put("updateTime", f.getLastModified( ));
                // 是否是目录
                m.put("isDir", f.isDir( ));
                if (f.isDir( )) {
                    // 文件类型
                    m.put("type", "dir");
                } else {
                    //是否支持在线编辑
                    boolean eflag = false;
                    // 是否支持在线查看
                    boolean pflag = false;

                    try {
                        if (FileTypeUtil.canEdit(new Tika( ).detect(new File(f.getAbsolutePath( ))))) {
                            eflag = true;
                            pflag = true;
                        }else if (FileTypeUtil.canOnlinePreview(new Tika( ).detect(new File(f.getAbsolutePath( ))))) {
                            pflag = true;
                        }
                        m.put("editable", eflag);
                        m.put("preview", pflag);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    String type;
                    // 文件地址
                    m.put("url", (dir.isEmpty( ) ? dir : (dir + SLASH)) + f.getFileName( ));
                    // 获取文件类型
                    String contentType = null;
                    String suffix = f.getFileName( ).substring(f.getFileName( ).lastIndexOf(".") + 1);

                    try {
                        contentType = new Tika( ).detect(new File(f.getAbsolutePath( )));
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    // 筛选文件类型
                    if (accept != null && !accept.trim( ).isEmpty( ) && !accept.equals("file")) {
                        if (contentType == null || !contentType.startsWith(accept + SLASH)) {
                            continue;
                        }
                        if (mExts != null) {
                            for (String ext : mExts) {
                                if (!f.getFileName( ).endsWith("." + ext)) {
                                    continue;
                                }
                            }
                        }
                    }

                    // 获取文件图标
                    m.put("type", getFileType(suffix, contentType));
                    // 是否有缩略图
                    String smUrl = "sm/" + (dir.isEmpty( ) ? dir : (dir + SLASH)) + f.getFileName( );
                    if (new File(fileDir + smUrl).exists( )) {
                        m.put("hasSm", true);
                        // 缩略图地址
                        m.put("smUrl", smUrl);
                    }
                }
                dataList.add(m);
            }

        }


        // 根据上传时间排序
        Collections.sort(dataList, new Comparator<Map>( ) {
            @Override
            public int compare(Map o1, Map o2) {

                long l1 = (long) o1.get("updateTime");
                long l2 = (long) o2.get("updateTime");
                return  l1>l2?1:-1;
            }
        });
        // 把文件夹排在前面
        Collections.sort(dataList, new Comparator<Map>( ) {
            @Override
            public int compare(Map o1, Map o2) {
                Boolean l1 = (boolean) o1.get("isDir");
                Boolean l2 = (boolean) o2.get("isDir");
                return l2.compareTo(l1);
            }
        });
        rs.put("code", 200);
        rs.put("msg", "查询成功");
        rs.put("data", dataList);
        return rs;
    }

    /**
     * 获取修改、上传信息ok
     *
     * @param file
     * @param curPos
     * @return Map
     */
    public Map update(MultipartFile file, String curPos) {
        curPos = curPos.substring(1) + SLASH;
        FDirNorm( );
        // 文件原始名称
        String originalFileName = file.getOriginalFilename( );
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String prefix = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        // 保存到磁盘
        File outFile;
        String path;

        int index = 1;
        path = curPos + originalFileName;
        outFile = new File(fileDir + path);
        while (outFile.exists( )) {
            path = curPos + prefix + "(" + index + ")." + suffix;
            outFile = new File(fileDir + path);
            index++;
        }
        try {
            if (!outFile.getParentFile( ).exists( )) {
                outFile.getParentFile( ).mkdirs( );
            }
            file.transferTo(outFile);
            Map rs = getRS(200, "上传成功", path);
//            //生成缩略图
//            if (useSm != null && useSm) {
//                // 获取文件类型
//                String contentType = null;
//                try {
//                    contentType = new Tika( ).detect(outFile);
//                } catch (IOException e) {
//                    e.printStackTrace( );
//                }
//                if (contentType != null && contentType.startsWith("image/")) {
//                    File smImg = new File(fileDir + "sm/" + path);
//                    if (!smImg.getParentFile( ).exists( )) {
//                        smImg.getParentFile( ).mkdirs( );
//                    }
//                    Thumbnails.of(outFile).scale(1f).outputQuality(0.25f).toFile(smImg);
//                    rs.put("smUrl", "sm/" + path);
//                }
//            }

            //更新树
            FileTree.fileInfo.add(path);
            return rs;
        } catch (Exception e) {
//            log.info(e.getMessage());
            return getRS(500, e.getMessage( ));
        }
    }

    /**
     * 重命名ok
     *
     * @param oldFile
     * @param newFile
     * @return Map
     */
    public Map rename(String oldFile, String newFile) {
        FDirNorm( );

        //在根目录的时候默认路径是/  这样与文件名中/+fName 叠加会产生//fname的情况
        if (oldFile.startsWith("//")) {
            oldFile = oldFile.substring(1);
            newFile = newFile.substring(1);
        }

        System.out.println("oldFile:" + oldFile + "\nnewFile:" + newFile);

        if (!StringUtils.isEmpty(oldFile) && !StringUtils.isEmpty(newFile)) {
            File f = new File(fileDir + oldFile);
            File smF = new File(fileDir + "sm/" + oldFile);
            File nFile = new File(fileDir + newFile);
            File nsmFile = new File(fileDir + "sm/" + newFile);
            if (f.renameTo(nFile)) {
                if (smF.exists( )) {
                    smF.renameTo(nsmFile);
                }
                //更新文件树
                FileTree.fileInfo.reName(oldFile, fileDir + newFile.substring(1));
                return getRS(200, "重命名成功", SLASH + newFile);
            }
        }
        return getRS(500, "重命名失败");
    }

    /**
     * 删除ok
     *
     * @param file
     * @return Map
     */
    public Map delete(String file) {
        FDirNorm( );
        if (file != null && !file.isEmpty( )) {
            File f = new File(fileDir + file);
            File smF = new File(fileDir + "sm/" + file);
            if (f.exists( )) {
                // 更新文件树
                FileTree.fileInfo.delete(file);
                // 文件
                if (f.isFile( )) {
                    if (f.delete( )) {
                        if (smF.exists( ) && smF.isFile( )) {
                            smF.delete( );
                        }
                        return getRS(200, "文件删除成功");
                    }
                } else {
                    // 目录
                    forDelFile(f);
                    if (smF.exists( ) && smF.isDirectory( )) {
                        forDelFile(smF);
                    }
                    return getRS(200, "目录删除成功");
                }

            } else {
                return getRS(500, "文件或目录不存在");
            }
        }
        return getRS(500, "文件或目录删除失败");
    }


    /**
     * 新建文件夹ok
     *
     * @param curPos
     * @param dirName
     * @return Map
     */
    public Map mkdir(String curPos, String dirName) {
        FDirNorm( );
        if (!StringUtils.isEmpty(curPos) && !StringUtils.isEmpty(dirName)) {
            curPos = curPos.substring(1);
            String dirPath = fileDir + curPos + SLASH + dirName;
            File f = new File(dirPath);
            if (f.exists( )) {
                return getRS(500, "目录已存在");
            }
            if (!f.exists( ) && f.mkdir( )) {
                FileTree.fileInfo.add(SLASH + dirName);
                return getRS(200, "创建成功");
            }
        }
        return getRS(500, "创建失败");
    }


    /**
     * 文件分享功能
     *
     * @param file
     * @param time
     * @return
     */
    public Map share(String file, int time) {
        // 若文件已经分享
        if (!CacheUtil.dataMap.isEmpty( )) {
            if (CacheUtil.dataMap.containsValue(file)) {
                Set<String> set = CacheUtil.dataExpireMap.keySet( );
                // 找出分享的key
                String key = null;
                for (String t : set) {
                    if (CacheUtil.get(t) != null && CacheUtil.get(t).equals(file)) {
                        key = t;
                        break;
                    }
                }
                // 是否在有效期内
                if (key != null) {
                    Date expireDate = CacheUtil.dataExpireMap.get(key);
                    if (expireDate != null && expireDate.compareTo(new Date( )) > 0) {
                        return getRS(200, "该文件已分享", domain + SLASH + "share?sid=" + key);
                    }
                }
            }
        }
        FDirNorm( );
        String sid = UUID.randomUUID( ).toString( );
        CacheUtil.put(sid, file, time);
        return getRS(200, "分享成功", domain + SLASH + "share?sid=" + sid);
    }


    /**
     * 分享页面信息封装
     *
     * @param sid
     * @param modelMap
     * @return
     */
    public String sharePage(String sid, ModelMap modelMap) {
        if (!CacheUtil.dataMap.isEmpty( )) {
            if (CacheUtil.dataMap.containsKey(sid)) {
                // 是否在有效期内
                Date expireDate = CacheUtil.dataExpireMap.get(sid);
                if (expireDate != null && expireDate.compareTo(new Date( )) > 0) {
                    String url = (String) CacheUtil.get(sid);
                    // 文件是否存在
                    File existFile = new File(fileDir + url);
                    if (!existFile.exists( )) {
                        modelMap.put("exists", false);
                        modelMap.put("msg", "该文件已不存在~");
                        return "share";
                    }
                    // 检测文件类型
                    String contentType = null;
                    String suffix = existFile.getName( ).substring(existFile.getName( ).lastIndexOf(".") + 1);
                    try {
                        contentType = new Tika( ).detect(existFile);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    // 获取文件图标、文件名、图片文件缩略图片地址、过期时间
                    modelMap.put("sid", sid);
                    modelMap.put("type", getFileType(suffix, contentType));
                    modelMap.put("exists", true);
                    modelMap.put("fileName", url.substring(url.lastIndexOf('/') + 1));
                    // 是否有缩略图
                    String smUrl = "sm/" + url;
                    if (new File(fileDir + smUrl).exists( )) {
                        modelMap.put("hasSm", true);
                        // 缩略图地址
                        modelMap.put("smUrl", "share/file/sm?sid=" + sid);
                    }
                    modelMap.put("expireTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(CacheUtil.dataExpireMap.get(sid)));
                    // 是否支持浏览器在线查看
                    boolean flag = false;
                    if (FileTypeUtil.canOnlinePreview(contentType)) {
                        flag = true;
                    }
                    modelMap.put("preview", flag);
                    return "share";
                }
            }
        }
        modelMap.put("exists", false);
        modelMap.put("msg", "分享不存在或已经失效~");
        return "share";
    }


    /**
     * 返回分享源文件或其缩略图页面或文件
     *
     * @param sid
     * @param download 是否下载
     * @param modelMap
     * @param response
     * @return
     */
    public String returnShareFileOrSm(String sid, boolean download, ModelMap modelMap, HttpServletResponse response) {
        String url = null;
        if (!CacheUtil.dataMap.isEmpty( )) {
            if (CacheUtil.dataMap.containsKey(sid)) {
                // 是否在有效期内
                Date expireDate = CacheUtil.dataExpireMap.get(sid);
                if (expireDate != null && expireDate.compareTo(new Date( )) > 0) {
                    url = (String) CacheUtil.get(sid);
                    // 文件是否存在
                    File existFile = new File(fileDir + url);
                    if (!existFile.exists( )) {
                        modelMap.put("msg", "该文件已不存在~");
                        return "error.html";
                    }
                } else {
                    modelMap.put("msg", "分享文件已过期");
                    return "error.html";
                }
            } else {
                modelMap.put("msg", "无效的sid");
                return "error.html";
            }
        }
        return getFile(url, download, response);
    }


    /**
     * 获取源文件或者缩略图文件,在这里修改逻辑关系
     *
     * @param p
     * @param download 是否下载
     * @param response
     * @return
     */
    public String getFile(String p, boolean download, HttpServletResponse response) {

        System.out.println("getFile............" + p);
        FDirNorm( );
        outputFile(fileDir + p, download, response);
        return null;
    }



    public String fileContent(String file, boolean download) {
        // 判断文件是否存在
        File inFile = new File(file);
        // 文件不存在
        if (!inFile.exists( )) {
            return null;
        }

        // 获取文件类型
        String contentType = null;
        try {
            contentType = new Tika( ).detect(inFile);
        } catch (IOException e) {
            e.printStackTrace( );
        }
        // 图片、文本文件,则在线查看
        if (FileTypeUtil.canEdit(contentType) && !download) {

            String content = "";
            // 读文本文件
            BufferedReader br = null;
            try {
                String s = "";
                br = new BufferedReader(new FileReader(inFile));

                    while ((s=br.readLine()) != null){
                        content+=s+"\n";
                    }
//                System.out.println(content );
                return  content;
            } catch (FileNotFoundException e) {
                e.printStackTrace( );
            } catch (IOException e) {
                e.printStackTrace( );
            } finally {
                try {
                    br.close( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }

        }
        return null;
    }

    /**
     * 输出文件流
     *
     * @param file
     * @param download 是否下载
     * @param response
     */
    private void outputFile(String file, boolean download, HttpServletResponse response) {
        // 判断文件是否存在
        File inFile = new File(file);
        // 文件不存在
        if (!inFile.exists( )) {
            PrintWriter writer = null;
            try {
                response.setContentType("text/html;charset=UTF-8");
                writer = response.getWriter( );
                writer.write("<!doctype html><title>404 Not Found</title><link rel=\"shorcut icon\" href=\"assets/images/logo.png\"><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">FMS Server</p>");
                writer.flush( );
            } catch (IOException e) {
                e.printStackTrace( );
            }
            return;
        }
        // 获取文件类型
        String contentType = null;
        try {
            contentType = new Tika( ).detect(inFile);
        } catch (IOException e) {
            e.printStackTrace( );
        }
        // 图片、文本文件,则在线查看
//        log.info("文件类型：" + contentType);
        if (FileTypeUtil.canOnlinePreview(contentType) && !download) {
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
        } else {
            // 其他文件,强制下载
            response.setContentType("application/force-download");
            String newName;
            try {
                newName = URLEncoder.encode(inFile.getName( ), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace( );
                newName = inFile.getName( );
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + newName);
        }
        // 输出文件流
        OutputStream os = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream( );
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        } finally {
            try {
                is.close( );
                os.close( );
            } catch (IOException e) {
                e.printStackTrace( );
            }
        }
    }


    /**
     * 获取当前日期
     */
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        return sdf.format(new Date( ));
    }

    /**
     * 递归删除目录下的文件以及目录
     *
     * @param file
     * @return
     */
    static boolean forDelFile(File file) {
        if (!file.exists( )) {
            return false;
        }
        if (file.isDirectory( )) {
            File[] files = file.listFiles( );
            for (File f : files) {
                forDelFile(f);
            }
        }
        return file.delete( );
    }

    /**
     * 获取文件类型
     *
     * @param suffix
     * @param contentType
     * @return
     */
    private String getFileType(String suffix, String contentType) {
        String type;
        if (FileTypeEnum.PPT.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.PPTX.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PPT.getName( );
        } else if (FileTypeEnum.DOC.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.DOCX.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.DOC.getName( );
        } else if (FileTypeEnum.XLS.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.XLSX.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.XLS.getName( );
        } else if (FileTypeEnum.PDF.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PDF.getName( );
        } else if (FileTypeEnum.HTML.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.HTM.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.HTM.getName( );
        } else if (FileTypeEnum.TXT.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.TXT.getName( );
        } else if (FileTypeEnum.MD.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.MD.getName( );
        } else if (FileTypeEnum.SWF.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.FLASH.getName( );
        } else if (FileTypeEnum.ZIP.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.RAR.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.SEVENZ.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.ZIP.getName( );
        } else if (contentType != null && contentType.startsWith(FileTypeEnum.AUDIO.getName( ) + SLASH)) {
            type = FileTypeEnum.MP3.getName( );
        } else if (contentType != null && contentType.startsWith(FileTypeEnum.VIDEO.getName( ) + SLASH)) {
            type = FileTypeEnum.MP4.getName( );
        } else {
            type = FileTypeEnum.FILE.getName( );
        }
        return type;
    }

    /**
     * 封装返回结果
     *
     * @param code
     * @param msg
     * @param url
     * @return Map
     */
    private Map getRS(int code, String msg, String url) {
        Map<String, Object> map = new HashMap<>( );
        map.put("code", code);
        map.put("msg", msg);
        if (url != null) {
            map.put("url", url);
        }
        return map;
    }

    /**
     * 封装返回结果
     *
     * @param code
     * @param msg
     * @return Map
     */
    private Map getRS(int code, String msg) {
        return getRS(code, msg, null);
    }


    /**
     * 使得fileDir比较规范
     */
    private void FDirNorm() {
        if (fileDir == null) {
            fileDir = SLASH;
        }
        if (!fileDir.endsWith(SLASH)) {
            fileDir += SLASH;
        }
    }


    /**
     * 拿到文本文件的文字内容，非文本文件返回null
     * @param map
     * @param p
     * @return
     */
    public HashMap<String, Object> getContextString(HashMap<String, Object> map,String p){
        String[] slist = p.split("/");
        LinkedList<String> dlist = new LinkedList<>( );

        for (int i = 0; i < slist.length; i++) {
            if (!slist[i].equals("")) {
                dlist.add(slist[i]);
            }
        }
        FileInfo fileInfo = FileTree.fileInfo.findChildFiles(dlist);
        System.out.println(fileInfo.getFileName( ));
        String type = "";
        type = fileInfo.getFileName( ).substring(fileInfo.getFileName( ).lastIndexOf(".") + 1);


        map.put("title", fileInfo.getFileName( ));
        map.put("type",type);
        map.put("updateTime",fileInfo.getLastModified());
        //由于Jquery在获取字符串的时候会自动吧反斜杠过滤，因此需要替换一下
        map.put("path",fileInfo.getAbsolutePath().replace('\\','/'));
        map.put("content", fileContent(fileInfo.getAbsolutePath( ), false));
        System.out.println(map.get("path") );
//        System.out.println(map.get("content") );
        return map;
    }
}
