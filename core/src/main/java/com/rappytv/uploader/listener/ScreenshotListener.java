package com.rappytv.uploader.listener;

import com.rappytv.uploader.UploaderAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.WriteScreenshotEvent;

public class ScreenshotListener {

    @Subscribe
    public void onScreenshot(WriteScreenshotEvent event) {
        Component component = UploaderAddon.prefix.copy().append(
            Component.translatable(
                "uploader.upload.upload",
                Style.empty()
                    .color(NamedTextColor.AQUA)
                    .hoverEvent(HoverEvent.showText(Component.translatable("uploader.upload.hover").color(NamedTextColor.GREEN)))
                    .clickEvent(ClickEvent.runCommand("/supload " + event.getDestination().getName()))
            )
        );

        Laby.references().chatExecutor().displayClientMessage(component);
    }
}
