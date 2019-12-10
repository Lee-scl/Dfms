package com.docker;

import com.docker.entity.FileInfo;
import com.docker.tasks.FileTree;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;

/**
 * Created by CHEN on 2019/12/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileInfoTest {

    /**
     * 走树，根据路径找到文件的方法测试
     */
    @org.junit.Test
    public void testFindChild() {


        LinkedList<String> dir = new LinkedList<>( );
        dir.add("安全帽");
        dir.add("文献");
        FileInfo info = FileTree.fileInfo.findChildFiles(dir);
        System.out.println(info.getFileName( ));
    }

}
