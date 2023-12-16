package com.rappytv.uploader.api.uploaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.Uploader;
import java.net.http.HttpResponse;

public class ZiplineUploader extends Uploader {

    public ZiplineUploader(UploaderAddon addon) {
        super("zipline", addon);
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
    public String getAuth() {
        return addon.configuration().zipline().auth();
    }

    @Override
    public int getStatus(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("code") ? object.get("code").getAsInt() : response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return response.statusCode();
        }
    }

    @Override
    public String getError(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.has("error") ? object.get("error").getAsString() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
