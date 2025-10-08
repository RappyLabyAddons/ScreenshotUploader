package com.rappytv.screenshotuploader.core.uploader.uploaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rappytv.screenshotuploader.api.ScreenshotUploaderTextures.SpriteUploaders;
import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.config.subconfig.ZiplineConfig;
import java.io.File;
import java.io.IOException;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.io.web.request.FormData;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.Response;
import org.jetbrains.annotations.NotNull;

public class ZiplineUploader extends Uploader<ZiplineConfig> {

    private final ScreenshotUploaderAddon addon;

    public ZiplineUploader(ScreenshotUploaderAddon addon) {
        super("zipline", "Zipline");
        this.addon = addon;
    }

    @Override
    public Icon getIcon() {
        return SpriteUploaders.ZIPLINE;
    }

    @Override
    public @NotNull ZiplineConfig getConfig() {
        return this.addon.configuration().zipline();
    }

    @Override
    public boolean validateConfig() {
        String base = this.getConfig().base().get();
        String auth = this.getConfig().auth().get();

        return !base.isBlank()
            && base.startsWith("https://")
            && !base.endsWith("/")
            && !auth.isBlank();
    }

    @Override
    public String uploadScreenshot(File file) throws UploadException {
        FormData fileData;

        try {
            fileData = FormData.builder()
                .name("file")
                .fileName(file.getName())
                .contentType("image/png")
                .value(file.toPath())
                .build();
        } catch (IOException e) {
            throw new UploadException(e, this);
        }

        Request<JsonObject> request = Request.ofGson(JsonObject.class)
            .url(this.getConfig().base().get() + "/api/upload")
            .method(Method.POST)
            .addHeader("Authorization", this.getConfig().auth().get())
            .addHeader("X-Zipline-Format", this.getConfig().nameFormat().get().toHeader())
            .form(fileData)
            .handleErrorStream();

        String domains = this.getConfig().domains().get();
        int compression = this.getConfig().compression().get();

        if(!domains.isBlank()) {
            request.addHeader("X-Zipline-Domain", domains);
        }
        if(compression > 0) {
            request.addHeader("X-Zipline-Image-Compression", compression);
        }

        Response<JsonObject> response = request.executeSync();

        if(response.hasException()) {
            throw new UploadException(response.exception(), this);
        }

        try {
            int statusCode = response.getStatusCode();
            JsonObject body = response.get();

            if(statusCode != 200) {
                String message;
                if (body.has("message")) {
                    message = body.get("message").getAsString();
                } else if (body.has("error")) {
                    message = body.get("error").getAsString();
                } else {
                    message = "Failed to upload file with status " + statusCode;
                }

                throw new UploadException(message, this);
            }

            if(body.has("files")) {
                JsonArray files = body.getAsJsonArray("files");
                if(!files.isEmpty()) {
                    return files.get(0).getAsJsonObject().get("url").getAsString();
                }
            }

            throw new UploadException("Response body does not contain the file link", this);
        } catch (IllegalArgumentException e) {
            throw new UploadException(e, this);
        }
    }
}
