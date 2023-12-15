package com.rappytv.e;

import net.labymod.api.addon.LabyAddon;

public class EAddon extends LabyAddon<EConfiguration> {

    @Override
    protected void enable() {
        registerSettingCategory();
    }

    @Override
    protected Class<? extends EConfiguration> configurationClass() {
        return EConfiguration.class;
    }
}
