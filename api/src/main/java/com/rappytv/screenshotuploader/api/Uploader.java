package com.rappytv.screenshotuploader.api;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.configuration.loader.Config;
import org.jetbrains.annotations.NotNull;
import java.io.File;

/**
 * A custom uploader which is choosable by the player to upload a screenshot
 * @param <C> The Uploader config. Use {@link EmptyConfig} if you don't need a config.
 */
public abstract class Uploader<C extends Config> {

    protected static final String DESCRIPTION = "Uploaded using the ScreenshotUploader LabyMod Addon";
    public static final EmptyConfig EMPTY_CONFIG = new EmptyConfig();

    private final String id;
    private final String name;

    /**
     * Constructs a new Screenshot Uploader
     * @param id The Uploader ID
     * @param name The nice name to display
     */
    public Uploader(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the Uploader ID
     * @return The Uploader ID
     */
    @NotNull
    public String getId() {
        return this.id;
    }

    /**
     * Get the nice name of the Uploader
     * @return The nice name of the Uploader
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Get the Uploader Icon
     * @return The Uploader Icon
     */
    @NotNull
    public abstract Icon getIcon();

    /**
     * Get the Uploader configuration
     * @return The Uploader configuration
     * @implNote Return {@link Uploader#EMPTY_CONFIG} if you don't need a config
     */
    @NotNull
    public abstract C getConfig();

    /**
     * Check if the config options are valid for uploading a screenshot
     * @return {@link true} if the config options are valid for uploading a screenshot; otherwise {@link false}
     */
    public abstract boolean validateConfig();

    /**
     * Upload a screenshot
     * @param file The screenshot file to uploade
     * @return The url where the screenshot can be accessed
     * @throws UploadException if something goes wrong
     * @implNote Wrap all exceptions using {@link UploadException} for the best error handling
     */
    public abstract String uploadScreenshot(File file) throws UploadException;

    /**
     * An empty config placeholder for Uploaders which don't need a config
     */
    public static class EmptyConfig extends Config {

    }
}
