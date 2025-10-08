package com.rappytv.screenshotuploader.core.config;

import com.rappytv.screenshotuploader.core.config.subconfig.ImgurConfig;
import com.rappytv.screenshotuploader.core.config.subconfig.XBackBoneConfig;
import com.rappytv.screenshotuploader.core.config.subconfig.ZiplineConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SpriteTexture("settings")
public class UploaderConfig extends AddonConfig {

    @SpriteSlot
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SpriteSlot(x = 1)
    @SwitchSetting
    private final ConfigProperty<Boolean> askBeforeDoubleUploads = new ConfigProperty<>(true);

    @SettingSection("uploaders")
    @IntroducedIn(namespace = "screenshotuploader", value = "1.0.4")
    @SpriteSlot(size = 32, y = 1)
    private final ImgurConfig imgur = new ImgurConfig();

    @SpriteSlot(size = 32, x = 1, y = 1)
    private final XBackBoneConfig xbackbone = new XBackBoneConfig();

    @SpriteSlot(size = 32, x = 2, y = 1)
    private final ZiplineConfig zipline = new ZiplineConfig();

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }

    public ConfigProperty<Boolean> askBeforeDoubleUploads() {
        return this.askBeforeDoubleUploads;
    }

    public ImgurConfig imgur() {
        return this.imgur;
    }

    public XBackBoneConfig xbackbone() {
        return this.xbackbone;
    }

    public ZiplineConfig zipline() {
        return this.zipline;
    }

}
