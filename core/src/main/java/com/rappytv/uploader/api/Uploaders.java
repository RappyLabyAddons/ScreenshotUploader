package com.rappytv.uploader.api;

public enum Uploaders {
    ESHARE,
    IMGUR,
    XBACKBONE,
    ZIPLINE;

    public Uploader getUploader() {
        return Uploader.get(this);
    }
}
