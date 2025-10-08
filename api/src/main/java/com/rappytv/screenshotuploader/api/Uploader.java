package com.rappytv.screenshotuploader.api;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.configuration.loader.Config;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public abstract class Uploader<C extends Config> {

    private final String id;
    private final String name;

    public Uploader(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public abstract Icon getIcon();

    @NotNull
    public abstract C getConfig();

    public abstract boolean validateConfig();

    public abstract String uploadScreenshot(File file) throws UploadException;

    public static class EmptyConfig extends Config {

    }
}
