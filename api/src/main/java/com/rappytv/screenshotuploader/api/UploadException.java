package com.rappytv.screenshotuploader.api;

import org.jetbrains.annotations.NotNull;

/**
 * This exception is thrown when an upload fails for whatever reason
 */
public class UploadException extends RuntimeException {

    private final Uploader<?> uploader;

    /**
     * Constructs a new uploader exception with a custom message
     * @param message The reason why the upload failed
     * @param uploader The uploader the upload failed with
     */
    public UploadException(@NotNull String message, @NotNull Uploader<?> uploader) {
        super(message);
        this.uploader = uploader;
    }

    /**
     * Constructs a new uploader exception with a different cause
     * @param cause The reason why the upload failed
     * @param uploader The uploader the upload failed with
     */
    public UploadException(@NotNull Throwable cause, @NotNull Uploader<?> uploader) {
        super(cause);
        this.uploader = uploader;
    }

    /**
     * Get the uploader with which the upload failed
     * @return The uploader the upload failed with
     */
    @NotNull
    public Uploader<?> getUploader() {
        return this.uploader;
    }
}
