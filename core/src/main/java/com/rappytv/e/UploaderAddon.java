package com.rappytv.e;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class UploaderAddon extends LabyAddon<UploaderConfig> {

    @Override
    protected void enable() {
        registerSettingCategory();
    }

    @Override
    protected Class<? extends UploaderConfig> configurationClass() {
        return UploaderConfig.class;
    }
}
