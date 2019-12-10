package com.docker.constant;

/**
 * 文件类型枚举
 * Created by CHEN on 2019/11/26.
 */
public enum FileTypeEnum {

    /**
     * 文件类型
     */
    PPT("ppt"),
    PPTX("pptx"),
    DOC("doc"),
    DOCX("docx"),
    XLS("xls"),
    XLSX("xlsx"),
    PDF("pdf"),
    XMIND("xmind"),
    CAJ("caj"),

    HTML("html"),
    HTM("htm"),
    TXT("txt"),
    MD("md"),
    JAVA("java"),
    C("c"),
    CPP("cpp"),
    PY("py"),
    XML("xml"),
    TEXT("text"),

    SWF("swf"),
    FLASH("flash"),
    ZIP("zip"),
    RAR("rar"),
    SEVENZ("7z"),
    AUDIO("audio"),
    MP3("mp3"),
    VIDEO("video"),
    MP4("mp4"),
    FILE("file"),
    IMAGE("image"),
    APPLICATION("application");

    private String name;

    FileTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
