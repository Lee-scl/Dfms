package com.docker.controller;

import com.docker.entity.User;
import com.docker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by CHEN on 2019/12/10.
 */
@Slf4j
@CrossOrigin
@Controller
public class UserController {
    @Autowired
    UserService userService;

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
        User user1 = userService.selUser(user.getUsername( ));
        if (user.getPassword( ).equals(user1.getPassword( ))) {
            session.setAttribute("LOGIN_USER", user);
            return "redirect:/";
        }
        return "redirect:/login";
    }
}
