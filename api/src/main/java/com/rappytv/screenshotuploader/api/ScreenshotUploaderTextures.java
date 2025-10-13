package com.rappytv.screenshotuploader.api;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class ScreenshotUploaderTextures {

    public static class SpriteUploaders {

        private static final ResourceLocation TEXTURE = ResourceLocation.create("screenshotuploader", "themes/vanilla/textures/settings.png");

        public static Icon CRAFTSHOT = Icon.sprite32(TEXTURE, 0, 1);
        public static Icon IMGUR = Icon.sprite32(TEXTURE, 1, 1);
        public static Icon XBACKBONE = Icon.sprite32(TEXTURE, 2, 1);
        public static Icon ZIPLINE = Icon.sprite32(TEXTURE, 3, 1);
    }

}
