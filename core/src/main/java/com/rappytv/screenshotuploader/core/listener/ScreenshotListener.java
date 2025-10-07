package com.rappytv.screenshotuploader.core.listener;

import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.WriteScreenshotEvent;

public class ScreenshotListener {

    @Subscribe
    public void onScreenshot(WriteScreenshotEvent event) {
        Component component = ScreenshotUploaderAddon.prefix().append(
            Component.translatable("screenshotuploader.upload.upload", NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(Component.translatable("screenshotuploader.upload.hover").color(NamedTextColor.GREEN)))
                .clickEvent(ClickEvent.runCommand("/supload " + event.getDestination().getName()))
        );

        Laby.references().chatExecutor().displayClientMessage(component);
    }
}
