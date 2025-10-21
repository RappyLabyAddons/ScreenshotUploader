package com.rappytv.screenshotuploader.core.uploader.uploaders;

import com.google.gson.JsonObject;
import com.rappytv.screenshotuploader.api.ScreenshotUploaderTextures.SpriteUploaders;
import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.config.subconfig.CraftShotConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.TokenStorage.Purpose;
import net.labymod.api.labyconnect.TokenStorage.Token;
import net.labymod.api.util.io.web.request.FormData;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftShotUploader extends Uploader<CraftShotConfig> {

    private static final String UPLOAD_ENDPOINT = "https://craftshot.net/v1/upload";

    private final ScreenshotUploaderAddon addon;

    public CraftShotUploader(ScreenshotUploaderAddon addon) {
        super("craftshot", "CraftShot");
        this.addon = addon;
    }

    @Override
    public Icon getIcon() {
        return SpriteUploaders.CRAFTSHOT;
    }

    @Override
    public @NotNull CraftShotConfig getConfig() {
        return this.addon.configuration().craftshot();
    }

    @Override
    public boolean validateConfig() {
        return true;
    }

    @Override
    public String uploadScreenshot(File file) throws UploadException {
        String token = this.getLabyConnectToken();
        if (token == null) {
            throw new UploadException("You're not connected to LabyConnect!", this);
        }

        List<FormData> formData = new ArrayList<>();
        formData.add(FormData.builder().name("access_token").value(token).build());

        String host = this.getHost();
        if (host != null && this.getConfig().addHost().get()) {
            formData.add(FormData.builder().name("server_ip").value(host).build());
        }

        try {
            formData.add(
                FormData.builder()
                    .name("screenshot")
                    .fileName("screenshot.png")
                    .contentType("image/png")
                    .value(file.toPath())
                    .build()
            );
        } catch (IOException e) {
            throw new UploadException(e, this);
        }

        Response<JsonObject> response = Request.ofGson(JsonObject.class)
            .url(UPLOAD_ENDPOINT)
            .method(Method.POST)
            .form(formData)
            .handleErrorStream()
            .executeSync();

        if(response.hasException()) {
            throw new UploadException(response.exception(), this);
        }

        try {
            int statusCode = response.getStatusCode();
            JsonObject data = response.get().get("data").getAsJsonObject();

            if(statusCode != 200) {
                if(data != null && data.has("message")) {
                    throw new UploadException(data.get("message").getAsString(), this);
                }

                throw new UploadException("Failed to upload file with status " + statusCode, this);
            }

            if(data != null && data.has("url")) {
                return data.get("url").getAsString();
            }

            throw new UploadException("Response body does not contain the file link", this);
        } catch (IllegalArgumentException e) {
            throw new UploadException(e, this);
        }
    }

    @Nullable
    private String getLabyConnectToken() {
        LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();
        if (session == null) {
            return null;
        }

        Token token = session.tokenStorage().getToken(Purpose.JWT, session.self().getUniqueId());

        if (token == null || token.isExpired()) {
            return null;
        }

        return token.getToken();
    }

    @Nullable
    private String getHost() {
        ServerData server = Laby.labyAPI().serverController().getCurrentServerData();
        if (server == null || server.address().getHost().isEmpty()) {
            return null;
        }
        return server.address().getHost();
    }
}
