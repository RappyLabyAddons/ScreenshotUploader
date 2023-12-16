package com.rappytv.uploader.api;

import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.uploaders.Uploader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ApiRequest {

    private final Charset charset = StandardCharsets.UTF_8;
    private boolean successful;
    private String uploadLink;
    protected String error;

    private final Uploader uploader;
    private final File file;

    public ApiRequest(UploaderAddon addon, File file) {
        this.uploader = addon.configuration().uploader();
        this.file = new File("D:\\windirs\\Dokumente\\@Minecraft\\Coding\\Labymod 4 Addons\\e\\run\\client\\screenshots\\2023-12-16_03.39.13.png");
    }

    public CompletableFuture<Void> sendAsyncRequest() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            String boundary = new BigInteger(128, new Random()).toString();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uploader.getUri()))
                .header("Content-Type", uploader.getMimeType(boundary))
                .header("Authorization", uploader.getAuth() != null ? uploader.getAuth() : "")
                .method(uploader.getMethod(), getBodyPublisher(boundary))
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
    private BodyPublisher getBodyPublisher(String boundary) throws IOException {
        byte[] newline = "\r\n".getBytes(charset);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(("--" + boundary).getBytes(charset));
        byteArrayOutputStream.write(newline);
        byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").getBytes(charset));
        byteArrayOutputStream.write(newline);
        byteArrayOutputStream.write(("Content-Type: image/png").getBytes(charset));
        byteArrayOutputStream.write(newline);
        byteArrayOutputStream.write(newline);
        byteArrayOutputStream.write(Files.readAllBytes(file.toPath()));
        byteArrayOutputStream.write(newline);
        byteArrayOutputStream.write(("--" + boundary + "--").getBytes(charset));

        return BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray());
    }
}