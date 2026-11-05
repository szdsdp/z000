package com.szd.z000.base.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Bytes转MultipartFile
 */
public class BytesToMultipartFile implements MultipartFile {
    String name, originalFilename, contentType;
    private byte[] fileBytes;
    boolean isEmpty;
    long size;

    public BytesToMultipartFile(byte[] fileBytes, String name, String originalFilename, String contentType, long size) {
        this.fileBytes = fileBytes;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.isEmpty = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() {
        return fileBytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(fileBytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(fileBytes);
    }
}
