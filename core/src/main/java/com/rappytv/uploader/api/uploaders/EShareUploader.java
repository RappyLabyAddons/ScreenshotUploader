package com.rappytv.uploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.Uploader;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;
import java.net.http.HttpResponse;

public class EShareUploader extends Uploader {

    public EShareUploader(UploaderAddon addon) {
        super("eshare", addon);
    }

    @Override
    public Icon getIcon() {
        return Icon.sprite32(icons, 0, 1);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getUri() {
        return "https://api.ebio.gg/api/share/upload";
    }

    @Override
    public String[] getAuth() {
        return new String[]{"api-key", addon.configuration().eshare().auth()};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("message")
                ? object.get("message").getAsString()
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

            return object.has("url") ? object.get("url").getAsString() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
