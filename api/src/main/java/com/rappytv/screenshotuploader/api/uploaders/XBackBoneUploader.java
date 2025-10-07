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

public class XBackBoneUploader extends Uploader {

    public XBackBoneUploader() {
        super("XBackBone");
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 2, 1);
    }

    @Override
    public String getUri() {
        return "";
    }

    @Override
    public String[] getHeaders() {
        return new String[]{};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("message") ? object.get("message").getAsString() : I18n.translate("screenshotuploader.upload.xError");
        } catch (Exception e) {
            return I18n.translate("screenshotuploader.upload.xError");
        }
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("url") ? object.get("url").getAsString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public MultipartData getMultipartData(File file) throws IOException {
        return MultipartData
            .newBuilder()
            .addFile("file", file.toPath(), "image/png")
            .addText(this.getHeaders()[0], this.getHeaders()[1])
            .build();
    }
}
