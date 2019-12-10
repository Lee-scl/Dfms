package com.docker.tasks;

import com.docker.entity.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 初始化FileInfo树
 * Created by CHEN on 2019/12/7.
 */
@Component
@Order(value = 1)
public class FileTree implements ApplicationRunner {

    @Value("${fs.dir}")
    public String fileDir;

    public static FileInfo fileInfo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("filedir = "+fileDir );
        File file  = new File(fileDir);
        fileInfo =  new FileInfo(file);
    }
}
