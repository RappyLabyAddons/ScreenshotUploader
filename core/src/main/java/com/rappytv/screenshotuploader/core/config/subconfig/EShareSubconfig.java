package com.rappytv.screenshotuploader.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class EShareSubconfig extends Config {

    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    public ConfigProperty<String> auth() {
        return this.auth;
    }
}
