package com.docker.util;

import org.apache.tika.Tika;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by CHEN on 2019/12/9.
 */
public class FileIOUtil {

    public static boolean write(String path, String content) {

        FileWriter writer;
        try {
            writer = new FileWriter(path);
            writer.write("");//清空原文件内容
            writer.write(content);
            writer.flush( );
            writer.close( );
            return true;
        } catch (IOException e) {
            e.printStackTrace( );
        }
        return false;
    }

    /**
     * 输出文件流
     *
     * @param file
     * @param download 是否下载
     * @param response
     */
    public static void outputFile(String file, boolean download, HttpServletResponse response) {
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
}
