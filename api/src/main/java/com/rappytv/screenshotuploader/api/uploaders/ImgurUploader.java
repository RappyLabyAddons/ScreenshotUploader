package com.rappytv.screenshotuploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.screenshotuploader.api.MultipartData;
import com.rappytv.screenshotuploader.api.Uploader;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;

public class ImgurUploader extends Uploader {

    public ImgurUploader() {
        super("Imgur");
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 1, 1);
    }

    @Override
    public String getUri() {
        return "https://api.imgur.com/3/upload";
    }

    @Override
    public String[] getHeaders() {
        return new String[]{};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject data = object.getAsJsonObject("data");

            return data != null && data.has("error")
                ? data.get("error").getAsString()
                : I18n.translate("screenshotuploader.upload.emptyError");
        } catch (Exception e) {
            return I18n.translate("screenshotuploader.upload.emptyError");
        }
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject data = object.getAsJsonObject("data");

            return data != null && data.has("link") ? data.get("link").getAsString() : "";
        } catch (Exception e) {
            return "";
        }
    }
    public MultipartData getMultipartData(File file) throws IOException {
        return MultipartData.newBuilder().addFile("image", file.toPath(), "image/png").build();
    }
}
