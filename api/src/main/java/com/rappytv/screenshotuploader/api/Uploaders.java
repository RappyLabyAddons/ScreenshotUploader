package com.rappytv.screenshotuploader.api;

public enum Uploaders {
    ESHARE,
    IMGUR,
    XBACKBONE,
    ZIPLINE;

    public Uploader getUploader() {
        return Uploader.get(this);
    }
}
