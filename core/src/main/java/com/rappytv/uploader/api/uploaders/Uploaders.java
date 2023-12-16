package com.rappytv.uploader.api.uploaders;

public enum Uploaders {
    ZIPLINE;

    public Uploader getUploader() {
        return Uploader.get(this);
    }
}
