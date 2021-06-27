package org.upgrad.upstac.shared;

import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MyMultiPartFile implements MultipartFile {
    private final String name;
    private String originalFilename;
    @Nullable
    private String contentType;
    private final byte[] content;



    public MyMultiPartFile(String name, InputStream contentStream) throws IOException {
        this(name, name, "image/png", FileCopyUtils.copyToByteArray(contentStream));
    }

    public MyMultiPartFile(String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable byte[] content) {
        this.name = name;
        this.originalFilename = originalFilename != null ? originalFilename : "";
        this.contentType = contentType;
        this.content = content != null ? content : new byte[0];
    }


    @Override
	public String getName() {
        return this.name;
    }

    @Override
	public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
	@Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Override
	public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
	public long getSize() {
        return this.content.length;
    }

    @Override
	public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
	public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }
}