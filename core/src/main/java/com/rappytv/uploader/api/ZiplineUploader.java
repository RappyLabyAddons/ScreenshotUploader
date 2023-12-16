package com.rappytv.uploader.api;

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
    public int getStatus(HttpResponse<String> response) {
        System.out.println(response.body());
        try {
            JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

            if(object.has("error")) error = object.get("error").getAsString();
            return object.has("code") ? object.get("code").getAsInt() : response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return response.statusCode();
        }
    }

    @Override
    public String resolveUrl(HttpResponse<String> response) {
        System.out.println(response.body());
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
