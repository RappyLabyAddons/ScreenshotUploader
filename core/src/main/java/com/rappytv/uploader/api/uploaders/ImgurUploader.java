package com.rappytv.uploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.MultipartData;
import com.rappytv.uploader.api.Uploader;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;

public class ImgurUploader extends Uploader {

    public ImgurUploader(UploaderAddon addon) {
        super("Imgur", addon);
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 1, 0);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getUri() {
        return "https://api.imgur.com/3/upload";
    }

    @Override
    public String[] getAuth() {
        return new String[]{"Authorization", "Client-ID (this is just a placeholder)"};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject data = object.getAsJsonObject("data");

            return data != null && data.has("error")
                ? data.get("error").getAsString()
                : I18n.translate("uploader.upload.emptyError");
        } catch (Exception e) {
            e.printStackTrace();
            return I18n.translate("uploader.upload.emptyError");
        }
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject data = object.getAsJsonObject("data");

            return data != null && data.has("link") ? data.get("link").getAsString() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public MultipartData getMultipartData(File file) throws IOException {
        return MultipartData.newBuilder().addFile("image", file.toPath(), "image/png").build();
    }
}
