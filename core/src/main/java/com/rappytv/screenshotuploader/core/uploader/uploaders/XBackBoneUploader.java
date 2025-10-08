package com.rappytv.screenshotuploader.core.uploader.uploaders;

import com.google.gson.JsonObject;
import com.rappytv.screenshotuploader.api.ScreenshotUploaderTextures.SpriteUploaders;
import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.config.subconfig.XBackBoneSubconfig;
import java.io.File;
import java.io.IOException;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.io.web.request.FormData;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.Response;
import org.jetbrains.annotations.NotNull;

public class XBackBoneUploader extends Uploader<XBackBoneSubconfig> {

    private final ScreenshotUploaderAddon addon;

    public XBackBoneUploader(ScreenshotUploaderAddon addon) {
        super("xbackbone", "XBackBone");
        this.addon = addon;
    }

    @Override
    public Icon getIcon() {
        return SpriteUploaders.XBACKBONE;
    }

    @Override
    public @NotNull XBackBoneSubconfig getConfig() {
        return this.addon.configuration().xbackbone();
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

        Response<JsonObject> response = Request.ofGson(JsonObject.class)
            .url(this.getConfig().base().get() + "/upload")
            .method(Method.POST)
            .addHeader("token", this.getConfig().auth().get())
            .form(fileData)
            .handleErrorStream()
            .executeSync();

        if(response.hasException()) {
            throw new UploadException(response.exception(), this);
        }

        try {
            int statusCode = response.getStatusCode();
            JsonObject body = response.get().getAsJsonObject();

            if(statusCode != 200) {
                if(body != null && body.has("message")) {
                    throw new UploadException(body.get("message").getAsString(), this);
                }

                throw new UploadException("Failed to upload file with status " + statusCode, this);
            }

            if(body != null && body.has("url")) {
                return body.get("url").getAsString();
            }

            throw new UploadException("Response body does not contain the file link", this);
        } catch (IllegalArgumentException e) {
            throw new UploadException(e, this);
        }
    }
}
