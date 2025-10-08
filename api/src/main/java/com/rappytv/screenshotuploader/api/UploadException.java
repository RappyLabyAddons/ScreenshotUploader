package com.rappytv.screenshotuploader.api;

public class UploadException extends RuntimeException {

    private final Uploader<?> uploader;

    public UploadException(String message, Uploader<?> uploader) {
        super(message);
        this.uploader = uploader;
    }

    public UploadException(Throwable cause, Uploader<?> uploader) {
        super(cause);
        this.uploader = uploader;
    }

    public Uploader<?> getUploader() {
        return this.uploader;
    }
}
