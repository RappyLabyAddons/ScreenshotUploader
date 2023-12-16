package com.rappytv.uploader;

import com.rappytv.uploader.api.uploaders.ZiplineUploader;
import com.rappytv.uploader.config.UploaderConfig;
import com.rappytv.uploader.listeners.ScreenshotListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class UploaderAddon extends LabyAddon<UploaderConfig> {

    public static Component prefix = Component.empty()
        .append(Component.text("UPLOADER", Style.empty().color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    @Override
    protected void enable() {
        registerSettingCategory();
        registerListener(new ScreenshotListener(this));

        loadUploaders();
    }

    @Override
    protected Class<? extends UploaderConfig> configurationClass() {
        return UploaderConfig.class;
    }

    private void loadUploaders() {
        new ZiplineUploader(this);
    }
}
