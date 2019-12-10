package com.docker.controller;

import com.docker.annotation.Login;
import com.docker.constant.FileTypeEnum;
import com.docker.entity.FileInfo;
import com.docker.entity.User;
import com.docker.service.FileService;
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
@Controller("ffff")
public class FileController {

    private static final String SLASH = "/";

    @Autowired
    FileService fileService;

    @Value("${fs.dir}")
    private String fileDir;

    @Value("${fs.useSm}")
    private Boolean useSm;


    //改成从数据库取
    @Value("${admin.uname}")
    private String uname;

    @Value("${admin.pwd}")
    private String pwd;




    /**
     * 登录页
     *
     * @return
     */
    @RequestMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    /**
     * 登录提交认证
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/auth")
    public String auth(User user, HttpSession session) {
        //此处可以加上登陆逻辑
        if (user.getUname( ).equals(uname) && user.getPwd( ).equals(pwd)) {
            session.setAttribute("LOGIN_USER", user);
            return "redirect:/";
        }
        return "redirect:/login";
    }

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
     * 查看/下载分享的源文件
     *
     * @param sid      分享sid
     * @param response
     * @return
     */
    @GetMapping("/share/file")
    public String shareFile(@RequestParam(value = "sid", required = true) String sid,
                            @RequestParam(value = "d", required = true) int d,
                            HttpServletResponse response,
                            ModelMap modelMap) {
        return fileService.returnShareFileOrSm(sid, d == 1 ? true : false, modelMap, response);
    }

    /**
     * 分享源文件的缩略图
     *
     * @param sid      分享sid
     * @param response
     * @return
     */
    @GetMapping("/share/file/sm")
    public String shareFileSm(@RequestParam(value = "sid", required = true) String sid,
                              HttpServletResponse response,
                              ModelMap modelMap) {
        return fileService.returnShareFileOrSm(sid, false, modelMap, response);
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
        return fileService.list(dir,accept,exts);
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

    /**
     * 分享文件展示页面
     *
     * @param sid      分享文件sid
     * @param modelMap
     * @return
     */
    @GetMapping("/share")
    public String sharePage(@RequestParam(value = "sid", required = true) String sid, ModelMap modelMap) {
        return fileService.sharePage(sid,modelMap);
    }

}
