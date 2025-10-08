package com.rappytv.screenshotuploader.core.uploader.uploaders;

import com.google.gson.JsonObject;
import com.rappytv.screenshotuploader.api.ScreenshotUploaderTextures.SpriteUploaders;
import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.config.subconfig.ImgurConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.io.web.request.FormData;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.Response;
import org.jetbrains.annotations.NotNull;

public class ImgurUploader extends Uploader<ImgurConfig> {

    private static final String UPLOAD_ENDPOINT = "https://api.imgur.com/3/upload";

    private final ScreenshotUploaderAddon addon;

    public ImgurUploader(ScreenshotUploaderAddon addon) {
        super("imgur", "Imgur");
        this.addon = addon;
    }

    @Override
    public Icon getIcon() {
        return SpriteUploaders.IMGUR;
    }

    @Override
    public @NotNull ImgurConfig getConfig() {
        return this.addon.configuration().imgur();
    }

    @Override
    public boolean validateConfig() {
        return true;
    }

    @Override
    public String uploadScreenshot(File file) throws UploadException {
        List<FormData> formData = new ArrayList<>();
        formData.add(
            FormData.builder()
                .name("title")
                .value("Screenshot taken by " + Laby.labyAPI().getName())
                .build()
        );
        if(this.getConfig().addDescription().get()) {
            formData.add(
                FormData.builder()
                    .name("description")
                    .value("Uploaded using the ScreenshotUploader LabyMod Addon")
                    .build()
            );
        }

        try {
            formData.add(
                FormData.builder()
                    .name("image")
                    .fileName(file.getName())
                    .contentType("image/png")
                    .value(file.toPath())
                    .build()
            );
        } catch (IOException e) {
            throw new UploadException(e, this);
        }

        // I honestly don't know how the client id stuff works so ignore all this stuff
        // When uploading images via the Imgur website it sends a client id via a query parameter
        // while the docs state that you need to pass it as a header so I just do both
        String auth = this.getConfig().auth().get();
        String clientId = !auth.isBlank() ? auth : Laby.labyAPI().getUniqueId().toString();

        Response<JsonObject> response = Request.ofGson(JsonObject.class)
            .url(UPLOAD_ENDPOINT + "?client_id=" + clientId)
            .method(Method.POST)
            .form(formData)
            .addHeader("Authorization", !auth.isBlank() ? "Client-ID " + auth : "")
            .handleErrorStream()
            .executeSync();

        if(response.hasException()) {
            throw new UploadException(response.exception(), this);
        }

        try {
            int statusCode = response.getStatusCode();
            JsonObject data = response.get().get("data").getAsJsonObject();

            if(statusCode != 200) {
                if(data != null && data.has("error")) {
                    throw new UploadException(data.get("error").getAsString(), this);
                }

                throw new UploadException("Failed to upload file with status " + statusCode, this);
            }

            if(data != null && data.has("link")) {
                return data.get("link").getAsString();
            }

            throw new UploadException("Response body does not contain the file link", this);
        } catch (IllegalArgumentException e) {
            throw new UploadException(e, this);
        }
    }
}
