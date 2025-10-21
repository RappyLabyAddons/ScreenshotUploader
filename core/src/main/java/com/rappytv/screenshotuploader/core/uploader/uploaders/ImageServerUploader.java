package com.rappytv.screenshotuploader.core.uploader.uploaders;

import com.rappytv.screenshotuploader.api.ScreenshotUploaderTextures.SpriteUploaders;
import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.config.subconfig.ImageServerConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.io.web.request.FormData;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.Response;
import org.jetbrains.annotations.NotNull;

public class ImageServerUploader extends Uploader<ImageServerConfig> {

    private static final String AUTHORIZED_ENDPOINT = "https://imageserver.pw/upload";
    private static final String ADDON_ENDPOINT = "https://imageserver.pw/upload/addon";

    private final ScreenshotUploaderAddon addon;

    public ImageServerUploader(ScreenshotUploaderAddon addon) {
        super("imageserver", "imageserver.pw");
        this.addon = addon;
    }

    @Override
    public Icon getIcon() {
        return SpriteUploaders.IMAGE_SERVER;
    }

    @Override
    public @NotNull ImageServerConfig getConfig() {
        return this.addon.configuration().imageServer();
    }

    @Override
    public boolean validateConfig() {
        return true;
    }

    @Override
    public String uploadScreenshot(File file) throws UploadException {
        List<FormData> formData = new ArrayList<>();

        if(this.getConfig().addDescription().get()) {
            formData.add(
                FormData.builder()
                    .name("title")
                    .value(DESCRIPTION)
                    .build()
            );
        }

        try {
            formData.add(
                FormData.builder()
                    .name("file")
                    .fileName("screenshot.png")
                    .contentType("image/png")
                    .value(file.toPath())
                    .build()
            );
        } catch (IOException e) {
            throw new UploadException(e, this);
        }

        Response<String> response = Request.ofString()
            .url(this.getConfig().auth().get().isBlank() ? ADDON_ENDPOINT : AUTHORIZED_ENDPOINT)
            .method(Method.POST)
            .addHeader("X-IMAGESERVER-AUTH-KEY", this.getConfig().auth().get())
            .form(formData)
            .handleErrorStream()
            .executeSync();

        if(response.hasException()) {
            throw new UploadException(response.exception(), this);
        }

        try {
            int statusCode = response.getStatusCode();
            String body = response.get();

            if(statusCode != 200) {
                throw new UploadException(body, this);
            }

            return body;
        } catch (IllegalArgumentException e) {
            throw new UploadException(e, this);
        }
    }
}
