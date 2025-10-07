package com.rappytv.screenshotuploader.api;

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
            MultipartData data = this.uploader.getMultipartData(this.file);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(this.uploader.getUri()))
                .header("Content-Type", data.getContentType())
                .headers(this.uploader.getHeaders())
                .method("POST", data.getBodyPublisher())
                .build();

            HttpClient client = HttpClient.newHttpClient();
            client
                .sendAsync(request, BodyHandlers.ofString())
                .thenAccept((response) -> {
                    this.successful = response.statusCode() >= 200 && response.statusCode() <= 299;
                    this.uploadLink = this.uploader.resolveUrl(response);
                    if(!this.successful) this.error = this.uploader.getError(response);
                    future.complete(null);
                })
                .exceptionally((e) -> {
                    future.completeExceptionally(e);
                    this.successful = false;
                    this.error = e.getMessage();
                    return null;
                });
        } catch (Exception e) {
            this.error = e.getMessage();
            this.successful = false;
            future.completeExceptionally(e);
        }

        return future;
    }

    public boolean isSuccessful() {
        return this.successful;
    }
    public String getUploadLink() {
        return this.uploadLink;
    }
    public String getError() {
        return this.error;
    }
}