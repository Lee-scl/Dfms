package com.docker.entity;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * 文件结构
 * Created by CHEN on 2019/11/26.
 */
public class FileInfo {

    @Value("${fs.dir}")
    private String fileDir;

    //文件名
    private String fileName;
    //绝对路径
    private String absolutePath;
    //父级路径
    private String parentPath;
    //修改日期
    private long lastModified;
    //文件类型（是否为文件夹、目录）
    private boolean isDir;

    private ArrayList<FileInfo> files;

    public FileInfo(String fileName, String absolutePath, String parentPath, long lastModified, boolean isDir, ArrayList<FileInfo> files) {
        this.fileName = fileName;
        this.absolutePath = absolutePath;
        this.parentPath = parentPath;
        this.lastModified = lastModified;
        this.isDir = isDir;
        this.files = files;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileDir='" + fileDir + '\'' +
                ", fileName='" + fileName + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", lastModified=" + lastModified +
                ", isDir=" + isDir +
                ", files=" + files +
                '}';
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public FileInfo(File file) {
        files = new ArrayList<>( );
        fileName = file.getName( );
        absolutePath = file.getAbsolutePath( );
        parentPath = file.getParent( );
        lastModified = file.lastModified( );
        isDir = file.isDirectory( );
        String[] list = file.list( );
        if (list != null) {
            for (String li : list
                    ) {
//                System.out.println(li);
//                System.out.println(new File(absolutePath + "\\" + li));
                files.add(new FileInfo(new File(absolutePath + "\\" + li)));
            }
        } else {
            files = null;
        }
    }

    public FileInfo() {
    }




    public String getTree(String tab) {
        String str = tab + "|" + fileName + "\n";
        if (files != null) {
            for (FileInfo fi : files
                    ) {
                str += tab + "\t" + fi.getTree(tab + "\t");
            }
        }

        return str;
    }


    /**
     * 走树的方法，递归调用即可
     *
     * @param dir
     * @return
     */
    public FileInfo findChildFiles(LinkedList<String> dir) {
        //这个文件名的路径是相对路径，所以当需要管理局部文件的时候，需要预处理
        String name = "";
        if (dir.size( ) == 0) {
            return this;
        } else if (dir.size( ) > 0) {
            name = dir.pop( );
        }

        if (name != "") {
            for (FileInfo fileInfo : this.files
                    ) {
                if (fileInfo.getFileName( ).equals(name)) {
                    return fileInfo.findChildFiles(dir);
                }
            }
        }
        return null;
    }

    public void add(String dir) {
        String[] s = dir.split("/");
        LinkedList<String> dlist = new LinkedList<>( );

        for (int i = 0; i < s.length - 1; i++) {
            if (!s[i].equals("")) {
                dlist.add(s[i]);
            }
        }

        ArrayList<FileInfo> flist = findChildFiles(dlist).getFiles( );
        File file = new File(this.getAbsolutePath( ) + dir);
        flist.add(new FileInfo(file.getName( ), file.getAbsolutePath( ), file.getParent( ), file.lastModified( ), file.isDirectory( ), null));

    }


    public void delete(String dir) {
        String[] s = dir.split("/");
        LinkedList<String> dlist = new LinkedList<>( );

        for (int i = 0; i < s.length - 1; i++) {
            if (!s[i].equals("")) {
                dlist.add(s[i]);
            }
        }

        ArrayList<FileInfo> flist = findChildFiles(dlist).getFiles( );
        int dindex = -1;
        for (int i = 0; i < flist.size( ); i++) {
            if (flist.get(i).getFileName( ).equals(s[s.length - 1])) {
                dindex = i;
                break;
            }
        }
        if (dindex >= 0) {
            flist.remove(dindex);
        }

    }

    public void reName(String oldFile, String newFile) {

        String[] s = oldFile.split("/");
        LinkedList<String> oldlis = new LinkedList<>( );

        for (int i = 0; i < s.length-1; i++) {
            if (!s[i].equals("")) {
                oldlis.add(s[i]);
            }
        }

        ArrayList<FileInfo> flist = findChildFiles(oldlis).getFiles( );
        int dindex = -1;
        for (int i = 0; i < flist.size( ); i++) {
            if (flist.get(i).getFileName( ).equals(s[s.length - 1])) {
                dindex = i;
                break;
            }
        }

        if (dindex >= 0) {
            flist.remove(dindex);
            flist.add(new FileInfo(new File(newFile)));
        }

    }


    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }


    public ArrayList<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileInfo> files) {
        this.files = files;
    }


}
