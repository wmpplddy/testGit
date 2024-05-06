package org.mvc;

import java.io.InputStream;

/**
 * 存储上传的文件信息
 */
public class MvcFile {
    private String fileName;
    private String contentType;
    private long size;
    private byte[] content ;
    private InputStream is;


    public MvcFile(String fileName, String contentType, long size, byte[] content, InputStream is) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
        this.is = is;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }
}
