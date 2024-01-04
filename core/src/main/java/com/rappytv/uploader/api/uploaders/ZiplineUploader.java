package com.rappytv.uploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.Uploader;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;
import java.net.http.HttpResponse;

public class ZiplineUploader extends Uploader {

    public ZiplineUploader(UploaderAddon addon) {
        super("Zipline", addon);
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
        return addon.configuration().zipline().base() + "/api/upload";
    }

    @Override
    public String[] getAuth() {
        return new String[]{"Authorization", addon.configuration().zipline().auth()};
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("error") ? object.get("error").getAsString() : I18n.translate("uploader.upload.ziplineError");
        } catch (Exception e) {
            e.printStackTrace();
            return I18n.translate("uploader.upload.ziplineError");
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
            e.printStackTrace();
            return "";
        }
    }
}
