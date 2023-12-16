package com.rappytv.e.listeners;

import com.rappytv.e.api.ZiplineUploader;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.CaptureScreenshotEvent;

public class ScreenshotListener {

    @Subscribe
    public void onScreenshot(CaptureScreenshotEvent event) {
        ZiplineUploader uploader = new ZiplineUploader("KEY", event.getDestination());
        uploader.sendAsyncRequest().thenAccept((result) -> {
            if(uploader.isSuccessful()) {
                Component copy = Component.translatable(
                    "screenshotuploader.copy.copy",
                    Style.builder()
                        .hoverEvent(HoverEvent.showText(Component.translatable("screenshotuploader.copy.hover"))).build()
                        .clickEvent(ClickEvent.copyToClipboard(uploader.getUploadLink()))
                );
                Component component = Component.translatable("screenshotuploader.copy.msg", copy);

                Laby.references().chatExecutor().displayClientMessage(component);
            } else {
                Laby.references().chatExecutor().displayClientMessage(uploader.getError());
            }
        }).exceptionally((e) -> {
            Laby.references().chatExecutor().displayClientMessage(e.getMessage());
            return null;
        });
    }
}
