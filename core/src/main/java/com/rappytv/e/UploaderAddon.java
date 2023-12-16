package com.rappytv.e;

import com.rappytv.e.listeners.ScreenshotListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class UploaderAddon extends LabyAddon<UploaderConfig> {

    @Override
    protected void enable() {
        registerSettingCategory();
        registerListener(new ScreenshotListener(this));
    }

    @Override
    protected Class<? extends UploaderConfig> configurationClass() {
        return UploaderConfig.class;
    }
}
