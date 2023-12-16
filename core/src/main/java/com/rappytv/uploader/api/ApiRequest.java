package com.rappytv.uploader.api;

import com.rappytv.uploader.UploaderAddon;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

public class ApiRequest {

    private boolean successful;
    private String uploadLink;
    protected String error;

    private final Uploader uploader;
    private final File file;

    public ApiRequest(UploaderAddon addon, File file) {
        this.uploader = addon.configuration().uploader();
        this.file = file;
    }

    public CompletableFuture<Void> sendAsyncRequest() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            MultipartData data = uploader.getMultipartData(file);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uploader.getUri()))
                .header("Content-Type", data.getContentType())
                .header(uploader.getAuth()[0], uploader.getAuth()[1])
                .method(uploader.getMethod(), data.getBodyPublisher())
                .build();

            HttpClient client = HttpClient.newHttpClient();
            client
                .sendAsync(request, BodyHandlers.ofString())
                .thenAccept((response) -> {
                    int status = uploader.getStatus(response);
                    successful = status >= 200 && status <= 299;
                    uploadLink = uploader.resolveUrl(response);
                    if(!successful) error = uploader.getError(response);
                    future.complete(null);
                })
                .exceptionally((e) -> {
                    future.completeExceptionally(e);
                    error = e.getMessage();
                    return null;
                });
        } catch (Exception e) {
            error = e.getMessage();
            future.completeExceptionally(e);
        }

        return future;
    }

    public boolean isSuccessful() {
        return successful;
    }
    public String getUploadLink() {
        return uploadLink;
    }
    public String getError() {
        return error;
    }
}