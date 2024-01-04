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

public class XBackBoneUploader extends Uploader {

    public XBackBoneUploader(UploaderAddon addon) {
        super("XBackBone", addon);
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 2, 1);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getUri() {
        return addon.configuration().xbackbone().base() + "/upload";
    }

    @Override
    public String[] getAuth() {
        return new String[]{"token", addon.configuration().xbackbone().auth()};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("message") ? object.get("message").getAsString() : I18n.translate("uploader.upload.xError");
        } catch (Exception e) {
            e.printStackTrace();
            return I18n.translate("uploader.upload.xError");
        }
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("url") ? object.get("url").getAsString() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public MultipartData getMultipartData(File file) throws IOException {
        return MultipartData
            .newBuilder()
            .addFile("file", file.toPath(), "image/png")
            .addText(getAuth()[0], getAuth()[1])
            .build();
    }
}
