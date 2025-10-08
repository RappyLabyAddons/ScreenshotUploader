package com.rappytv.screenshotuploader.core.uploader.uploaders;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rappytv.screenshotuploader.api.ScreenshotUploaderTextures.SpriteUploaders;
import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.api.Uploader.EmptyConfig;
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

public class ImgurUploader extends Uploader<EmptyConfig> {

    private static final String UPLOAD_ENDPOINT = "https://api.imgur.com/3/upload";

    public ImgurUploader() {
        super("imgur", "Imgur");
    }

    @Override
    public Icon getIcon() {
        return SpriteUploaders.IMGUR;
    }

    @Override
    public @NotNull EmptyConfig getConfig() {
        return new EmptyConfig();
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
        formData.add(
            FormData.builder()
                .name("description")
                .value("Uploaded using the ScreenshotUploader LabyMod Addon")
                .build()
        );

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

        Response<JsonObject> response = Request.ofGson(JsonObject.class)
            .url(UPLOAD_ENDPOINT + "?client_id=" + Laby.labyAPI().getUniqueId())
            .method(Method.POST)
            .form(formData)
            .handleErrorStream()
            .executeSync();

        if(response.hasException()) {
            throw new UploadException(response.exception(), this);
        }

        JsonElement body = response.get();

        try {
            int statusCode = response.getStatusCode();
            JsonObject data = body.getAsJsonObject().get("data").getAsJsonObject();

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
