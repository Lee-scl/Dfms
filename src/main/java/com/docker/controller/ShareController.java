package com.docker.controller;

import com.docker.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by CHEN on 2019/12/10.
 */
@Slf4j
@CrossOrigin
@Controller
public class ShareController {

    @Autowired
    FileService fileService;
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
