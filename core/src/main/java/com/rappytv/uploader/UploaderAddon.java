package com.rappytv.uploader;

import com.rappytv.uploader.api.uploaders.EShareUploader;
import com.rappytv.uploader.api.uploaders.ImgurUploader;
import com.rappytv.uploader.api.uploaders.XBackBoneUploader;
import com.rappytv.uploader.api.uploaders.ZiplineUploader;
import com.rappytv.uploader.command.UploadCommand;
import com.rappytv.uploader.config.UploaderConfig;
import com.rappytv.uploader.listener.ScreenshotListener;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class UploaderAddon extends LabyAddon<UploaderConfig> {

    public static Component prefix = Component.empty()
        .append(Component.text("UPLOADER", Style.empty().color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("uploader", new SemanticVersion("1.0.3"), "2024-03-09"));
    }

    @Override
    protected void enable() {
        registerSettingCategory();
        registerCommand(new UploadCommand(this));
        registerListener(new ScreenshotListener());

        loadUploaders();
    }

    @Override
    protected Class<? extends UploaderConfig> configurationClass() {
        return UploaderConfig.class;
    }

    private void loadUploaders() {
        new EShareUploader(this);
        new ImgurUploader(this);
        new XBackBoneUploader(this);
        new ZiplineUploader(this);
    }
}
