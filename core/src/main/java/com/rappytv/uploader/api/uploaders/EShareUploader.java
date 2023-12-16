package com.rappytv.uploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.Uploader;
import java.net.http.HttpResponse;

public class EShareUploader extends Uploader {

    public EShareUploader(UploaderAddon addon) {
        super("eshare", addon);
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
    public int getStatus(HttpResponse<String> response) {
        return response.statusCode();
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("message") ? object.get("message").getAsString() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
