package com.rappytv.uploader.api;

import com.rappytv.uploader.UploaderAddon;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class Uploader {

    private static final Map<String, Uploader> uploaders = new HashMap<>();
    protected final UploaderAddon addon;

    public Uploader(String name, UploaderAddon addon) {
        this.addon = addon;
        uploaders.put(name.toLowerCase(), this);
    }

    public static Uploader get(Uploaders uploader) {
        return get(uploader.name());
    }

    public static Uploader get(String id) {
        return uploaders.get(id.toLowerCase());
    }

    public abstract String getMethod();
    public abstract String getUri();
    public abstract String[] getAuth();
    public abstract int getStatus(HttpResponse<String> response);
    public abstract String getError(HttpResponse<String> response);
    public abstract String resolveUrl(HttpResponse<String> response);
    public MultipartData getMultipartData(File file) throws IOException {
        return MultipartData.newBuilder().addFile("file", file.toPath(), "image/png").build();
    }
}
