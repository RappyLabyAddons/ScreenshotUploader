package com.rappytv.e.api;

import com.rappytv.e.UploaderConfig;
import net.labymod.api.util.I18n;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public abstract class ApiRequest {

    private final Charset charset = StandardCharsets.UTF_8;
    private boolean successful;
    private String uploadLink;
    protected String error;

    private final String key;
    private final File file;

    public ApiRequest(String key, File file) {
        this.key = key;
        this.file = new File("D:\\windirs\\Dokumente\\@Minecraft\\Coding\\Labymod 4 Addons\\e\\run\\client\\screenshots\\2023-12-16_00.11.04.png");
    }

    public CompletableFuture<Void> sendAsyncRequest() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            String boundary = new BigInteger(128, new Random()).toString();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://media.rappytv.com" + getPath()))
                .header("Content-Type", getMimeType(boundary))
                .header("Authorization", key != null ? key : "")
                .method(getMethod(), getBodyPublisher(boundary))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            client
                .sendAsync(request, BodyHandlers.ofString())
                .thenAccept((response) -> {
                    int status = getStatus(response);
                    successful = status >= 200 && status <= 299;
                    uploadLink = resolveUrl(response);
                    future.complete(null);
                })
                .exceptionally((e) -> {
                    future.completeExceptionally(e);
                    error = UploaderConfig.exceptions ? e.getMessage() : I18n.translate("screenshotuploader.upload.uploadError");
                    return null;
                });
        } catch (Exception e) {
            error = UploaderConfig.exceptions ? e.getMessage() : I18n.translate("screenshotuploader.upload.uploadError");
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
    public abstract String getMethod();
    public abstract String getPath();
    public abstract int getStatus(HttpResponse<String> response);
    public abstract String resolveUrl(HttpResponse<String> response);
    public String getMimeType(String boundary) {
        return "multipart/form-data; boundary=" + boundary;
    }
}