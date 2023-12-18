package com.rappytv.uploader.command;

import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.api.ApiRequest;
import com.rappytv.uploader.api.Uploader;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextDecoration;
import java.io.File;

public class UploadCommand extends Command {

    private final UploaderAddon addon;

    public UploadCommand(UploaderAddon addon) {
        super("supload");
        this.addon = addon;
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        Uploader uploader = addon.configuration().uploader();
        if(uploader.getAuth()[1].isBlank()) {
            displayMessage(UploaderAddon.prefix.copy().append(Component.translatable("uploader.upload.noToken", NamedTextColor.RED)));
            return true;
        }
        if(args.length < 1) {
            displayMessage(UploaderAddon.prefix.copy().append(Component.translatable("uploader.upload.file", NamedTextColor.RED)));
            return true;
        }
        File file = new File(System.getProperty("user.dir") + "/screenshots/" + args[0]);
        if(!file.exists()) {
            displayMessage(UploaderAddon.prefix.copy().append(Component.translatable("uploader.upload.file", NamedTextColor.RED)));
            return true;
        }
        displayMessage(UploaderAddon.prefix.copy().append(Component.translatable("uploader.upload.uploading", NamedTextColor.GRAY)));
        ApiRequest request = new ApiRequest(uploader, file);
        request.sendAsyncRequest().thenAccept((result) -> {
            if(request.isSuccessful()) {
                Component copy = Component.translatable(
                    "uploader.upload.copy",
                    Style.empty()
                        .color(NamedTextColor.AQUA)
                        .decorate(TextDecoration.BOLD)
                        .hoverEvent(HoverEvent.showText(Component.translatable("uploader.upload.hover").color(NamedTextColor.GREEN)))
                        .clickEvent(ClickEvent.copyToClipboard(!request.getUploadLink().isBlank() ? request.getUploadLink() : ""))
                );
                Component component = Component.translatable(
                    "uploader.upload.uploaded",
                    !request.getUploadLink().isBlank() ? copy : Component.text("")
                ).color(NamedTextColor.GRAY);

                displayMessage(UploaderAddon.prefix.copy().append(component));
            } else {
                displayMessage(
                    UploaderAddon.prefix.copy().append(Component.text(
                        request.getError(),
                        NamedTextColor.RED
                    ))
                );
            }
        }).exceptionally((e) -> {
            displayMessage(
                UploaderAddon.prefix.copy().append(Component.text(
                    e.getMessage(),
                    NamedTextColor.RED
                ))
            );
            return null;
        });
        return true;
    }
}
