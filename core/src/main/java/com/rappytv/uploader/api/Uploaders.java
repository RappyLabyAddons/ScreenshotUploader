package com.rappytv.uploader.api;

public enum Uploaders {
    ZIPLINE;

    public Uploader getUploader() {
        return Uploader.get(this);
    }
}
