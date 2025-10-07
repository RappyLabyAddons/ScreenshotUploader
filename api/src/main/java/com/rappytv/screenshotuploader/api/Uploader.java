package com.rappytv.screenshotuploader.api;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public abstract class Uploader {

    private static final Map<String, Uploader> uploaders = new HashMap<>();
    protected static final ResourceLocation icons = ResourceLocation.create("screenshotuploader", "themes/vanilla/textures/settings.png");
    private final String name;

    public Uploader(String name) {
        this.name = name;
        uploaders.put(name.toLowerCase(), this);
    }

    public static Uploader get(Uploaders uploader) {
        return get(uploader.name());
    }

    public static Uploader get(String id) {
        return uploaders.get(id.toLowerCase());
    }

    public String getName() {
        return this.name;
    }

    public abstract Icon getIcon();
    public abstract String getUri();
    public abstract String[] getHeaders();
    public abstract String getError(HttpResponse<String> response);
    public abstract String resolveUrl(HttpResponse<String> response);
    public MultipartData getMultipartData(File file) throws IOException {
        return MultipartData.newBuilder().addFile("file", file.toPath(), "image/png").build();
    }
}
