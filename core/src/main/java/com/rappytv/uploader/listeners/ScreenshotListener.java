package com.rappytv.uploader.listeners;

import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.UploaderConfig;
import com.rappytv.uploader.api.ApiRequest;
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
        ApiRequest uploader = new ApiRequest(addon, event.getDestination());
        uploader.sendAsyncRequest().thenAccept((result) -> {
            if(uploader.isSuccessful()) {
                Component copy = Component.translatable(
                    "uploader.upload.copy",
                    Style.builder()
                        .color(NamedTextColor.AQUA)
                        .decorate(TextDecoration.BOLD)
                        .hoverEvent(HoverEvent.showText(Component.translatable("uploader.upload.hover").color(NamedTextColor.AQUA))).build()
                        .clickEvent(ClickEvent.copyToClipboard(!uploader.getUploadLink().isBlank() ? uploader.getUploadLink() : ""))
                );
                Component component = Component.translatable(
                    "uploader.upload.msg",
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
                    UploaderConfig.exceptions ? e.getMessage() : I18n.translate("uploader.upload.uploadError"),
                    NamedTextColor.RED
                ))
            );
            return null;
        });
    }
}
