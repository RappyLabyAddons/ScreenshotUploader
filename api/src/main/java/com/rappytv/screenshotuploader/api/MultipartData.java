package com.rappytv.screenshotuploader.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class MultipartData {

    public static class Builder {

        private final String boundary;
        private final Charset charset = StandardCharsets.UTF_8;
        private final List<MimedFile> files = new ArrayList<>();
        private final Map<String, String> texts = new LinkedHashMap<>();

        private Builder() {
            this.boundary = new BigInteger(128, new Random()).toString();
        }

        public Builder addFile(String name, Path path, String mimeType) {
            this.files.add(new MimedFile(name, path, mimeType));
            return this;
        }

        public Builder addText(String name, String text) {
            this.texts.put(name, text);
            return this;
        }

        public MultipartData build() throws IOException {
            MultipartData mimeMultipartData = new MultipartData();
            mimeMultipartData.boundary = this.boundary;

            byte[] newline = "\r\n".getBytes(this.charset);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (MimedFile f : this.files) {
                byteArrayOutputStream.write(("--" + this.boundary).getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"" + f.name + "\"; filename=\"" + f.path.getFileName() + "\"").getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(("Content-Type: " + f.mimeType).getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(Files.readAllBytes(f.path));
                byteArrayOutputStream.write(newline);
            }
            for (Entry<String, String> entry : this.texts.entrySet()) {
                byteArrayOutputStream.write(("--" + this.boundary).getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"").getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(entry.getValue().getBytes(this.charset));
                byteArrayOutputStream.write(newline);
            }
            byteArrayOutputStream.write(("--" + this.boundary + "--").getBytes(this.charset));

            mimeMultipartData.bodyPublisher = BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray());
            return mimeMultipartData;
        }

        public record MimedFile(String name, Path path, String mimeType) {}
    }

    private String boundary;
    private BodyPublisher bodyPublisher;

    private MultipartData() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public BodyPublisher getBodyPublisher() {
        return this.bodyPublisher;
    }

    public String getContentType() {
        return "multipart/form-data; boundary=" + this.boundary;
    }
}