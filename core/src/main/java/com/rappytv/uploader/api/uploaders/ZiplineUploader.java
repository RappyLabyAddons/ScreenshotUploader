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
        return Icon.sprite32(icons, 3, 1);
    }

    @Override
    public String getUri() {
        return addon.configuration().zipline().base() + "/api/upload";
    }

    @Override
    public String[] getHeaders() {
        return new String[]{
            "Authorization", addon.configuration().zipline().auth(),
            "Format", addon.configuration().zipline().nameFormat().name().toUpperCase(),
            "Image-Compression-Percent", addon.configuration().zipline().compression() + "",
            "Zws", addon.configuration().zipline().zeroWidth() + "",
            "Override-Domain", addon.configuration().zipline().domains(),
            "Embed", addon.configuration().zipline().embed() + ""
        };
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
