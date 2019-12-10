package com.docker;

import com.docker.entity.FileInfo;
import com.docker.entity.User;
import com.docker.mapper.UserMapper;
import com.docker.tasks.FileTree;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;

/**
 * Created by CHEN on 2019/12/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisConnectionTest {


    @Autowired
    UserMapper mapper;

    /**
     * mybatis测试
     */
    @org.junit.Test
    public void mybatisConnection() {
        User user = mapper.selUser("root");
        System.out.println(user);
    }

}
