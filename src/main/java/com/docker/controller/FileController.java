package com.docker.controller;

import com.docker.annotation.Login;
import com.docker.constant.FileTypeEnum;
import com.docker.entity.FileInfo;
import com.docker.entity.User;
import com.docker.service.FileService;
import com.docker.service.UserService;
import com.docker.tasks.FileTree;
import com.docker.util.CacheUtil;
import com.docker.util.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.standard.expression.Each;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件控制器
 * Created by CHEN on 2019/11/26.
 */
@Slf4j
@CrossOrigin
@Controller
public class FileController {

    private static final String SLASH = "/";

    @Autowired
    FileService fileService;

    @Value("${fs.dir}")
    private String fileDir;

    @Value("${fs.useSm}")
    private Boolean useSm;


    /**
     * 首页
     *
     * @return
     */
    @Login
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    /**
     * 上传文件
     *
     * @param file   文件
     * @param curPos 上传文件时所处的目录位置
     * @return Map
     */
    @Login
    @ResponseBody
    @PostMapping("/file/upload")
    public Map upload(@RequestParam MultipartFile file, @RequestParam String curPos) {
        return fileService.update(file, curPos);
    }


    /**
     * 查看/下载源文件
     *
     * @param p        文件全路径
     * @param d        是否下载,1-下载
     * @param response
     * @return
     */
    @Login
    @GetMapping("/file")
    public String file(@RequestParam("p") String p,
                       @RequestParam(value = "d", required = true) int d,
                       HttpServletResponse response) {
        return fileService.getFile(p, d == 1 ? true : false, response);
    }


    /**
     * 查看缩略图
     *
     * @param p        文件全名
     * @param response
     * @return
     */
    @Login
    @GetMapping("/file/sm")
    public String fileSm(@RequestParam("p") String p, HttpServletResponse response) {
        return fileService.getFile(p, false, response);
    }


    /**
     * 获取全部文件、文件列表
     *
     * @param dir
     * @param accept
     * @param exts
     * @return Map
     */
    @Login
    @ResponseBody
    @RequestMapping("/api/list")
    public Map list(String dir, String accept, String exts) {
        return fileService.list(dir, accept, exts);
    }


    /**
     * 删除
     *
     * @param file
     * @return Map
     */
    @Login
    @ResponseBody
    @RequestMapping("/api/del")
    public Map del(String file) {
        return fileService.delete(file);
    }

    /**
     * 重命名
     *
     * @param oldFile
     * @param newFile
     * @return Map
     */
    @Login
    @ResponseBody
    @RequestMapping("/api/rename")
    public Map rename(String oldFile, String newFile) {
        return fileService.rename(oldFile, newFile);
    }


    /**
     * 新建文件夹
     *
     * @param curPos
     * @param dirName
     * @return Map
     */
    @Login
    @ResponseBody
    @RequestMapping("/api/mkdir")
    public Map mkdir(String curPos, String dirName) {
        return fileService.mkdir(curPos, dirName);
    }

    @Login
    @ResponseBody
    @RequestMapping("/api/mkdirFile")
    public Map mkdirFile(String curPos, String FileName) {

        if (!FileTypeUtil.getFileType(FileName.split("\\.")[1] ,null).equals("file")){
            return  fileService.mkdir(curPos,FileName);
        }
        return  fileService.getRS(500, "无法创建该类型");
    }
    /**
     * 分享文件
     *
     * @param file 文件
     * @param time 有效时间(分钟)
     * @return Map
     */
    @Login
    @ResponseBody
    @PostMapping("/api/share")
    public Map share(String file, int time) {
        return fileService.share(file, time);
    }


}
