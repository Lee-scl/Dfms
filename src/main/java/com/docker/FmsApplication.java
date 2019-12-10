package com.docker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 入口
 * Created by CHEN on 2019/11/26.
 */
@MapperScan(value = "com.docker.mapper")
@SpringBootApplication
public class FmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmsApplication.class, args);
    }

}

