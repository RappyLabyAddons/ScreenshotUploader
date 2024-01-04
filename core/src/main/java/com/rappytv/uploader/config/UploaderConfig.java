package com.rappytv.uploader.config;

import com.rappytv.uploader.config.subconfig.EShareSubconfig;
import com.rappytv.uploader.config.subconfig.XBackBoneSubconfig;
import com.rappytv.uploader.config.subconfig.ZiplineSubconfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
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
    @SwitchSetting
    private final ConfigProperty<Boolean> askBeforeDoubleUploads = new ConfigProperty<>(true);
    @SettingSection("uploaders")
    @SpriteSlot(size = 32, y = 1)
    private final EShareSubconfig eshare = new EShareSubconfig();
    @SpriteSlot(size = 32, y = 1, x = 2)
    private final XBackBoneSubconfig xbackbone = new XBackBoneSubconfig();
    @SpriteSlot(size = 32, y = 1, x = 3)
    private final ZiplineSubconfig zipline = new ZiplineSubconfig();

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public boolean askBeforeDoubleUploads() {
        return askBeforeDoubleUploads.get();
    }
    public EShareSubconfig eshare() {
        return eshare;
    }
    public XBackBoneSubconfig xbackbone() {
        return xbackbone;
    }
    public ZiplineSubconfig zipline() {
        return zipline;
    }
}
