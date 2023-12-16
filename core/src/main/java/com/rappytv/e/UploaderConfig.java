package com.rappytv.e;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class UploaderConfig extends AddonConfig {

    public static boolean exceptions;

    public UploaderConfig() {
        exceptions = javaExceptions.get();
        javaExceptions.addChangeListener((value) -> exceptions = value);
    }

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SwitchSetting
    private final ConfigProperty<Boolean> javaExceptions = new ConfigProperty<>(false);
    @TextFieldSetting
    private final ConfigProperty<String> ziplineKey = new ConfigProperty<>("");

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public String ziplineKey() {
        return ziplineKey.get();
    }
}
