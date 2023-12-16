package com.rappytv.uploader.api;

public enum Uploaders {
    ESHARE,
    ZIPLINE;

    public Uploader getUploader() {
        return Uploader.get(this);
    }
}
