package com.rappytv.screenshotuploader.core.uploader;

import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.api.UploaderRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.models.Implements;
import org.jetbrains.annotations.Nullable;

@Singleton
@Implements(UploaderRegistry.class)
public class DefaultUploaderRegistry implements UploaderRegistry {

    private final Map<String, Uploader<? extends Config>> uploaders = new HashMap<>();

    @Override
    public void registerUploader(Uploader<? extends Config> uploader) throws IllegalArgumentException {
        if(this.uploaders.containsKey(uploader.getId())) {
            throw new IllegalStateException("Uploader " + uploader.getName() + " is already registered");
        }
        this.uploaders.put(uploader.getId(), uploader);
    }

    @Override
    public Collection<Uploader<? extends Config>> getUploaders() {
        return Collections.unmodifiableCollection(this.uploaders.values());
    }

    @Override
    public @Nullable Uploader<? extends Config> getUploader(String id) {
        return this.uploaders.get(id);
    }
}
