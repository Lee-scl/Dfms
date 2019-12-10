package com.docker.constant;


/**
 * 可浏览文件详细内容的类型枚举
 * Created by CHEN on 2019/11/26.
 */
public enum ContentTypeEnum {

    /**
     * pdf文件
     */
    APPLICATION_PDF("application/pdf"),

    /**
     * 文本文件,md
     */
    TXT_PLAIN("text/plain"),
    TXT_MD("text/x-web-markdown"),
    /**
     * 图片
     */
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_GIF("image/gif"),

    /**
     * 视频
     */
    VIDEO_MP4("video/mp4"),

    /**
     * 音频
     */
    AUDIO_MPEG("audio/mpeg"),

    /**
     * html文件
     */
    TEXT_HTML("text/html"),
    APPLICATION_XHTML_XML("application/xhtml+xml");

    private String name;

    ContentTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
