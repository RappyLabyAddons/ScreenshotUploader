package com.rappytv.uploader.config;

import com.rappytv.uploader.api.Uploader;
import com.rappytv.uploader.api.Uploaders;
import com.rappytv.uploader.config.subconfig.EShareSubconfig;
import com.rappytv.uploader.config.subconfig.ZiplineSubconfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SpriteTexture("settings")
public class UploaderConfig extends AddonConfig {

    @SpriteSlot(size = 32)
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SpriteSlot(size = 32, x = 1)
    @DropdownSetting
    private final ConfigProperty<Uploaders> uploader = new ConfigProperty<>(Uploaders.ZIPLINE);
    @SettingSection("uploaders")
    @SpriteSlot(size = 32, y = 1)
    private final EShareSubconfig eshare = new EShareSubconfig();
    @SpriteSlot(size = 32, y = 1, x = 1)
    private final ZiplineSubconfig zipline = new ZiplineSubconfig();

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public Uploader uploader() {
        return uploader.get().getUploader();
    }
    public EShareSubconfig eshare() {
        return eshare;
    }
    public ZiplineSubconfig zipline() {
        return zipline;
    }
}
