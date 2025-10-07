package com.rappytv.screenshotuploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.screenshotuploader.api.Uploader;
import java.net.http.HttpResponse;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;

public class EShareUploader extends Uploader {

    public EShareUploader() {
        super("EShare");
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 0, 1);
    }

    @Override
    public String getUri() {
        return "https://api.ebio.gg/api/share/upload";
    }

    @Override
    public String[] getHeaders() {
        return new String[]{};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("message")
                ? object.get("message").getAsString()
                : I18n.translate("screenshotuploader.upload.emptyError");
        } catch (Exception e) {
            return I18n.translate("screenshotuploader.upload.emptyError");
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
}
