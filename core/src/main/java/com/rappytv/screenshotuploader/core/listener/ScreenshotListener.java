package com.rappytv.screenshotuploader.core.listener;

import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.WriteScreenshotEvent;

public class ScreenshotListener {

    @Subscribe
    public void onScreenshot(WriteScreenshotEvent event) {
        if(event.getPhase() != Phase.POST) return;
        String fileName = event.getDestination().getName();

        Component component = Component.empty()
            .append(ScreenshotUploaderAddon.prefix())
            .append(Component.translatable("screenshotuploader.upload.madeScreenshot"))
            .append(Component.space())
            .append(
                Component.translatable("screenshotuploader.upload.uploadQuestion")
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.runCommand("/supload " + fileName))
                    .hoverEvent(HoverEvent.showText(Component.translatable(
                        "screenshotuploader.upload.upload",
                        NamedTextColor.DARK_PURPLE,
                        Component.text(fileName)
                    )))
            );

        Laby.references().chatExecutor().displayClientMessage(component);
    }
}
