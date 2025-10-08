package com.rappytv.screenshotuploader.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class ImgurConfig extends Config {

    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    @Exclude
    private final ConfigProperty<Boolean> addDescription = new ConfigProperty<>(true);

    public ConfigProperty<String> auth() {
        return this.auth;
    }

    public ConfigProperty<Boolean> addDescription() {
        return this.addDescription;
    }

}
