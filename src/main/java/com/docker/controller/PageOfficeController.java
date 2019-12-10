package com.docker.controller;

import com.docker.annotation.Login;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 问题待解决，暂时不实现，导包了会好找不到的问题，官网没答案
 * java.lang.ClassNotFoundException: com.zhuozhengsoft.pageoffice.PageOfficeCtrl
 * Created by CHEN on 2019/12/8.
 */
@Slf4j
@CrossOrigin
@Controller
public class PageOfficeController {


    private String poSysPath="d:/lic/";

    private String poPassWord="111111";

    @Login
    @RequestMapping(value="/index", method= RequestMethod.GET)
    public ModelAndView showIndex(){
        ModelAndView mv = new ModelAndView("Index");
        return mv;
    }

    @Login
    @RequestMapping(value="/word", method=RequestMethod.GET)
    public ModelAndView showWord(HttpServletRequest request, Map<String,Object> map){

        System.out.println("aaaaaaaaaaaaaaaaa" );
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
        poCtrl.setServerPage("/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("保存","Save",1);//添加自定义保存按钮
        poCtrl.setSaveFilePage("/save");//设置处理文件保存的请求方法
        //打开word
        poCtrl.webOpen("d:\\test.doc",OpenModeType.docAdmin,"张三");
        map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));

        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }
    @Login
    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response){
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile("d:\\" + fs.getFileName());
        fs.close();
    }



}
