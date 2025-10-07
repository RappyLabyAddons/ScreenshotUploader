package com.rappytv.screenshotuploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.screenshotuploader.api.Uploader;
import java.net.http.HttpResponse;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;

public class ZiplineUploader extends Uploader {

    public ZiplineUploader() {
        super("Zipline");
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 3, 1);
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

            return object.has("error") ? object.get("error").getAsString() : I18n.translate("screenshotuploader.upload.ziplineError");
        } catch (Exception e) {
            return I18n.translate("screenshotuploader.upload.ziplineError");
        }
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("files")
                ? object.getAsJsonArray("files").get(0).getAsString()
                : "";
        } catch (Exception e) {
            return "";
        }
    }
}
