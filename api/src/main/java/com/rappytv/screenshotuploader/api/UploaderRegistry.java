package com.rappytv.screenshotuploader.api;

import net.labymod.api.configuration.loader.Config;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

@Referenceable
public interface UploaderRegistry {

    void registerUploader(@NotNull Uploader<? extends Config> uploader);

    Collection<Uploader<? extends Config>> getUploaders();
}
