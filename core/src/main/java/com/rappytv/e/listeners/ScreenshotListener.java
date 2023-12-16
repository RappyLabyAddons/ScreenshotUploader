package com.rappytv.e.listeners;

import com.rappytv.e.UploaderAddon;
import com.rappytv.e.UploaderConfig;
import com.rappytv.e.api.ZiplineUploader;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.CaptureScreenshotEvent;
import net.labymod.api.util.I18n;

public class ScreenshotListener {

    private final UploaderAddon addon;

    public ScreenshotListener(UploaderAddon addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onScreenshot(CaptureScreenshotEvent event) {
        ZiplineUploader uploader = new ZiplineUploader(addon.configuration().ziplineKey(), event.getDestination());
        uploader.sendAsyncRequest().thenAccept((result) -> {
            if(uploader.isSuccessful()) {
                Component copy = Component.translatable(
                    "screenshotuploader.upload.copy",
                    Style.builder()
                        .color(NamedTextColor.AQUA)
                        .decorate(TextDecoration.BOLD)
                        .hoverEvent(HoverEvent.showText(Component.translatable("screenshotuploader.upload.hover").color(NamedTextColor.AQUA))).build()
                        .clickEvent(ClickEvent.copyToClipboard(!uploader.getUploadLink().isBlank() ? uploader.getUploadLink() : ""))
                );
                Component component = Component.translatable(
                    "screenshotuploader.upload.msg",
                    !uploader.getUploadLink().isBlank() ? copy : Component.text("")
                ).color(NamedTextColor.GRAY);

                Laby.references().chatExecutor().displayClientMessage(UploaderAddon.prefix.copy().append(component));
            } else {
                Laby.references().chatExecutor().displayClientMessage(
                    UploaderAddon.prefix.copy().append(Component.text(
                        uploader.getError(),
                        NamedTextColor.RED
                    ))
                );
            }
        }).exceptionally((e) -> {
            Laby.references().chatExecutor().displayClientMessage(
                UploaderAddon.prefix.copy().append(Component.text(
                    UploaderConfig.exceptions ? e.getMessage() : I18n.translate("screenshotuploader.upload.uploadError"),
                    NamedTextColor.RED
                ))
            );
            return null;
        });
    }
}
