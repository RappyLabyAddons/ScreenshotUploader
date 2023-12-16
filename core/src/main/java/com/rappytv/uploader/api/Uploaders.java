package com.rappytv.uploader.api;

public enum Uploaders {
    ESHARE,
    XBACKBONE,
    ZIPLINE;

    public Uploader getUploader() {
        return Uploader.get(this);
    }
}
