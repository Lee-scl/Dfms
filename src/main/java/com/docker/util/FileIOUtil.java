package com.docker.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by CHEN on 2019/12/9.
 */
public class FileIOUtil {

    public static boolean write(String path, String content) {

        FileWriter writer;
        try {
            writer = new FileWriter(path);
            writer.write("");//清空原文件内容
            writer.write(content);
            writer.flush( );
            writer.close( );
            return true;
        } catch (IOException e) {
            e.printStackTrace( );
        }
        return false;
    }


}
