package com.docker.service;

import com.docker.entity.User;
import com.docker.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CHEN on 2019/12/10.
 */
@Service
public class UserService {


    @Autowired
    UserMapper mapper;

    public User selUser(String username){
        return  mapper.selUser(username);
    }


}
