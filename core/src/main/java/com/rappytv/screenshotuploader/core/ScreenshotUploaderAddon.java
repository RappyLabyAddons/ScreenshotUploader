package com.rappytv.screenshotuploader.core;

import com.rappytv.screenshotuploader.api.uploaders.EShareUploader;
import com.rappytv.screenshotuploader.api.uploaders.ImgurUploader;
import com.rappytv.screenshotuploader.api.uploaders.XBackBoneUploader;
import com.rappytv.screenshotuploader.api.uploaders.ZiplineUploader;
import com.rappytv.screenshotuploader.core.command.UploadCommand;
import com.rappytv.screenshotuploader.core.config.UploaderConfig;
import com.rappytv.screenshotuploader.core.listener.ScreenshotListener;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class ScreenshotUploaderAddon extends LabyAddon<UploaderConfig> {

    private static final Component PREFIX = Component.empty()
        .append(Component.text("UPLOADER", NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("screenshotuploader", new SemanticVersion("1.0.3"), "2024-03-09"));
    }

    @Override
    protected void enable() {
        this.registerSettingCategory();
        this.registerCommand(new UploadCommand(this));
        this.registerListener(new ScreenshotListener());

        this.loadUploaders();
    }

    @Override
    protected Class<? extends UploaderConfig> configurationClass() {
        return UploaderConfig.class;
    }

    public static Component prefix() {
        return PREFIX.copy();
    }

    private void loadUploaders() {
        new EShareUploader();
        new ImgurUploader();
        new XBackBoneUploader();
        new ZiplineUploader();
    }
}
