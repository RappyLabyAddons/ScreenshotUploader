package com.rappytv.screenshotuploader.core.listener;

import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.ui.activity.UploadActivity;
import java.io.File;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.WriteScreenshotEvent;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;

public class ScreenshotListener {

    private static final String TRANSLATION_KEY = "screenshotuploader.event.";

    @Subscribe
    public void onScreenshot(WriteScreenshotEvent event) {
        if(event.getPhase() != Phase.POST) return;
        String fileName = event.getDestination().getName();

        Component component = Component.empty()
            .append(ScreenshotUploaderAddon.prefix())
            .append(Component.translatable(TRANSLATION_KEY + "madeScreenshot"))
            .append(Component.space())
            .append(
                Component.translatable(TRANSLATION_KEY + "uploadQuestion")
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.runCommand("/supload " + fileName))
                    .hoverEvent(HoverEvent.showText(Component.translatable(
                        "screenshotuploader.command.uploadFile",
                        NamedTextColor.DARK_PURPLE,
                        Component.text(fileName)
                    )))
            );

        Laby.references().chatExecutor().displayClientMessage(component);

        if(!Laby.labyAPI().minecraft().isIngame()) {
            NotificationButton button = NotificationButton.of(
                Component.translatable(TRANSLATION_KEY + "notification.button"),
                () -> Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                    File file = event.getDestination();

                    if(!file.exists()) {
                        Notification.builder()
                            .title(Component.translatable("screenshotuploader.notification.error"))
                            .text(Component.translatable("screenshotuploader.command.fileNotFound"))
                            .buildAndPush();
                        return;
                    }
                    Laby.labyAPI().minecraft().minecraftWindow().displayScreen(
                        new UploadActivity(file)
                    );
                })
            );

            Notification.builder()
                .title(Component.translatable(TRANSLATION_KEY + "notification.title"))
                .text(Component.translatable(TRANSLATION_KEY + "notification.description"))
                .addButton(button)
                .buildAndPush();
        }
    }
}
