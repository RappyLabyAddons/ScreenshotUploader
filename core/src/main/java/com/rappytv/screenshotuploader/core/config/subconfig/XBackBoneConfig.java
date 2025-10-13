package com.rappytv.screenshotuploader.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.CustomTranslation;

public class XBackBoneConfig extends Config {

    @CustomTranslation("screenshotuploader.settings.general.baseUrl")
    @TextFieldSetting
    private final ConfigProperty<String> base = new ConfigProperty<>("https://example.org");

    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    public ConfigProperty<String> base() {
        return this.base;
    }

    public ConfigProperty<String> auth() {
        return this.auth;
    }
}
