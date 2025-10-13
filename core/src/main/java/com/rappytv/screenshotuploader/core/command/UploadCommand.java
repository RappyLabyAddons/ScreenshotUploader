package com.rappytv.screenshotuploader.core.command;

import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import com.rappytv.screenshotuploader.core.ui.activity.UploadActivity;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;

public class UploadCommand extends Command {

    private final ScreenshotUploaderAddon addon;
    private final Set<String> history = new HashSet<>();

    public UploadCommand(ScreenshotUploaderAddon addon) {
        super("supload");
        this.addon = addon;

        this.translationKey("screenshotuploader.command");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(args.length < 1) {
            this.displayMessage(ScreenshotUploaderAddon.prefix().append(Component.translatable(
                this.getTranslationKey("fileNotFound"),
                NamedTextColor.RED
            )));
            return true;
        }
        File file = new File(System.getProperty("user.dir") + "/screenshots/" + args[0]);
        if(!file.exists()) {
            this.displayMessage(ScreenshotUploaderAddon.prefix().append(Component.translatable(
                this.getTranslationKey("fileNotFound"),
                NamedTextColor.RED
            )));
            return true;
        }
        if(this.history.contains(file.getName()) && this.addon.configuration().askBeforeDoubleUploads().get()) {
            if(args.length < 2 || !args[1].equalsIgnoreCase("force")) {
                Component component = Component.empty()
                    .append(ScreenshotUploaderAddon.prefix())
                    .append(Component.translatable(
                        this.getTranslationKey("alreadyUploaded"),
                        NamedTextColor.RED
                    ))
                    .append(Component.space())
                    .append(
                        Component.translatable(this.getTranslationKey("openAnyway"))
                            .color(NamedTextColor.AQUA)
                            .decorate(TextDecoration.UNDERLINED)
                            .hoverEvent(HoverEvent.showText(Component.translatable(
                                this.getTranslationKey("uploadFile"),
                                NamedTextColor.GREEN,
                                Component.text(file.getName())
                            )))
                            .clickEvent(ClickEvent.runCommand(String.format(
                                "/%s %s force",
                                prefix,
                                file.getName()
                            )))
                    );

                this.displayMessage(component);
                return true;
            }
        }
        this.history.add(file.getName());
        Laby.labyAPI().minecraft().executeOnRenderThread(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new UploadActivity(file))
        );
        return true;
    }
}
