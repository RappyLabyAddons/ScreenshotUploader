package com.rappytv.screenshotuploader.api;

import net.labymod.api.configuration.loader.Config;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

@Referenceable
public interface UploaderRegistry {

    /**
     * Registers a new uploader
     * @param uploader The uploader to register
     * @throws IllegalArgumentException when trying to register an Uploader twice
     */
    void registerUploader(@NotNull Uploader<? extends Config> uploader) throws IllegalArgumentException;

    /**
     * Get all uploaders
     * @return A collection of Uploaders
     */
    Collection<Uploader<? extends Config>> getUploaders();

    /**
     * Gets a specific registered uploader by its ID
     * @param id The Uploader ID
     * @return The uploader or null if the ID is not registered
     */
    @Nullable
    Uploader<? extends Config> getUploader(String id);
}
