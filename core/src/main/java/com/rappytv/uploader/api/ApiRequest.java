package com.rappytv.uploader.api;

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

    public ApiRequest(Uploader uploader, File file) {
        this.uploader = uploader;
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
                .method("POST", data.getBodyPublisher())
                .build();

            HttpClient client = HttpClient.newHttpClient();
            client
                .sendAsync(request, BodyHandlers.ofString())
                .thenAccept((response) -> {
                    successful = response.statusCode() >= 200 && response.statusCode() <= 299;
                    uploadLink = uploader.resolveUrl(response);
                    if(!successful) error = uploader.getError(response);
                    future.complete(null);
                })
                .exceptionally((e) -> {
                    future.completeExceptionally(e);
                    successful = false;
                    error = e.getMessage();
                    return null;
                });
        } catch (Exception e) {
            error = e.getMessage();
            successful = false;
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