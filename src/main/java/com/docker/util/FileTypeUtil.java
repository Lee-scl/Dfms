package com.docker.util;

import com.docker.constant.ContentTypeEnum;
import com.docker.constant.FileTypeEnum;
import org.springframework.util.StringUtils;

/**
 * @author cinco
 * @description 文件类型检测工具类
 * @date 2019-1-28
 */
public class FileTypeUtil {

    /**
     * 根据内容类型检测文件是否支持浏览器在线预览
     *
     * @param contentType
     * @return boolean
     */
    public static final boolean canOnlinePreview(String contentType) {
        boolean flag = false;
        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        if (contentType.equals(ContentTypeEnum.APPLICATION_PDF.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_PLAIN.getName( )) ||

                contentType.equals(ContentTypeEnum.TXT_C.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_JAVA.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_PY.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_XML.getName( )) ||

                contentType.equals(ContentTypeEnum.IMAGE_GIF.getName( )) ||
                contentType.equals(ContentTypeEnum.IMAGE_JPEG.getName( )) ||
                contentType.equals(ContentTypeEnum.IMAGE_PNG.getName( )) ||
                contentType.equals(ContentTypeEnum.VIDEO_MP4.getName( )) ||
                contentType.equals(ContentTypeEnum.AUDIO_MPEG.getName( )) ||
                contentType.equals(ContentTypeEnum.TEXT_HTML.getName( )) ||
                contentType.equals(ContentTypeEnum.APPLICATION_XHTML_XML.getName( ))) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据内容类型检测文件是否支持浏览器在线修改
     *
     * @param contentType
     * @return boolean
     */
    public static final boolean canEdit(String contentType) {
        boolean flag = false;
        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        if (contentType.equals(ContentTypeEnum.TXT_PLAIN.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_MD.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_C.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_JAVA.getName( ))||
                contentType.equals(ContentTypeEnum.TXT_PY.getName( )) ||
                contentType.equals(ContentTypeEnum.TXT_XML.getName( ))) {
            flag = true;
        }
        return flag;
    }


    /**
     * 获取文件类型
     *
     * @param suffix
     * @param contentType
     * @return
     */
    public static String getFileType(String suffix, String contentType) {
        String type;
        if (FileTypeEnum.PPT.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.PPTX.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PPT.getName( );
        } else if (FileTypeEnum.DOC.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.DOCX.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.DOC.getName( );
        } else if (FileTypeEnum.XLS.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.XLSX.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.XLS.getName( );
        } else if (FileTypeEnum.PDF.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PDF.getName( );
        } else if (FileTypeEnum.XMIND.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.XMIND.getName( );
        }else if (FileTypeEnum.CAJ.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.CAJ.getName( );
        }

        else if (FileTypeEnum.HTML.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.HTM.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.HTM.getName( );
        } else if (FileTypeEnum.TXT.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.TXT.getName( );
        } else if (FileTypeEnum.MD.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.MD.getName( );
        } else if (FileTypeEnum.JAVA.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.JAVA.getName( );
        } else if (FileTypeEnum.C.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.C.getName( );
        } else if (FileTypeEnum.CPP.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.CPP.getName( );
        } else if (FileTypeEnum.PY.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.PY.getName( );


        } else if (FileTypeEnum.SWF.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.FLASH.getName( );
        } else if (FileTypeEnum.ZIP.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.RAR.getName( ).equalsIgnoreCase(suffix) || FileTypeEnum.SEVENZ.getName( ).equalsIgnoreCase(suffix)) {
            type = FileTypeEnum.ZIP.getName( );
        } else if (contentType != null && contentType.startsWith(FileTypeEnum.AUDIO.getName( ) + "/")) {
            type = FileTypeEnum.MP3.getName( );
        } else if (contentType != null && contentType.startsWith(FileTypeEnum.VIDEO.getName( ) + "/")) {
            type = FileTypeEnum.MP4.getName( );
        } else {
            type = FileTypeEnum.FILE.getName( );
        }
        return type;
    }
}
