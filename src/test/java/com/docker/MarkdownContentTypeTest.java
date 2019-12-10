package com.docker;

import com.docker.entity.FileInfo;
import com.docker.tasks.FileTree;
import org.apache.tika.Tika;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by CHEN on 2019/12/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkdownContentTypeTest {


    /**
     * 测试md的contenttype
     * text/x-web-markdown
     */
    @org.junit.Test
    public void testMD() {

    try {
        System.out.println(new Tika( ).detect(new File("C:\\Users\\YBJB\\Desktop\\安全帽\\待阅资料_CH.md")));

    }catch (Exception e){

        e.printStackTrace();
    }


    }
}
