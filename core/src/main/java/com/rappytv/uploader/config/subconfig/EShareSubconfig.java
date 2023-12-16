package com.rappytv.uploader.config.subconfig;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.Debounce;

public class EShareSubconfig extends Config {

    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    public String auth() {
        return auth.get();
    }
}
