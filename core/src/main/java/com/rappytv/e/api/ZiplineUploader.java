package com.rappytv.e.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.net.http.HttpResponse;

public class ZiplineUploader extends ApiRequest {

    public ZiplineUploader(String key, File file) {
        super(key, file);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getPath() {
        return "/api/upload";
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            return object.getAsJsonArray("files").get(0).getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
