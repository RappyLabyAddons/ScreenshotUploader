package com.rappytv.screenshotuploader.core;

import com.rappytv.screenshotuploader.api.UploaderRegistry;
import com.rappytv.screenshotuploader.api.generated.ReferenceStorage;
import com.rappytv.screenshotuploader.core.command.UploadCommand;
import com.rappytv.screenshotuploader.core.config.UploaderConfig;
import com.rappytv.screenshotuploader.core.listener.ScreenshotListener;
import com.rappytv.screenshotuploader.core.uploader.uploaders.ImgurUploader;
import com.rappytv.screenshotuploader.core.uploader.uploaders.XBackBoneUploader;
import com.rappytv.screenshotuploader.core.uploader.uploaders.ZiplineUploader;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.logging.Logging;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class ScreenshotUploaderAddon extends LabyAddon<UploaderConfig> {

    private static final Component PREFIX = Component.empty()
        .append(Component.text("UPLOADER", NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    private static ScreenshotUploaderAddon INSTANCE;

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("screenshotuploader", new SemanticVersion("1.0.3"), "2024-03-09"));
    }

    @Override
    protected void enable() {
        INSTANCE = this;

        this.registerSettingCategory();
        this.registerCommand(new UploadCommand(this));
        this.registerListener(new ScreenshotListener());
        uploaderRegistry().registerUploader(new ImgurUploader());
        uploaderRegistry().registerUploader(new XBackBoneUploader(this));
        uploaderRegistry().registerUploader(new ZiplineUploader(this));
    }

    @Override
    protected Class<? extends UploaderConfig> configurationClass() {
        return UploaderConfig.class;
    }

    public static Component prefix() {
        return PREFIX.copy();
    }

    public static Logging logging() {
        return INSTANCE.logger();
    }

    public static UploaderRegistry uploaderRegistry() {
        return ((ReferenceStorage) INSTANCE.referenceStorageAccessor()).uploaderRegistry();
    }
}
