package com.rappytv.screenshotuploader.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class CraftShotConfig extends Config {

    @SwitchSetting
    private final ConfigProperty<Boolean> addHost = new ConfigProperty<>(true);

    public ConfigProperty<Boolean> addHost() {
        return this.addHost;
    }

}
