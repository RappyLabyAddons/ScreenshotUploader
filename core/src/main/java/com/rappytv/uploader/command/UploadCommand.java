package com.rappytv.uploader.command;

import com.rappytv.uploader.UploaderAddon;
import com.rappytv.uploader.activity.UploadActivity;
import com.rappytv.uploader.api.Uploader;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
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
        Laby.labyAPI().minecraft().executeNextTick(() -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new UploadActivity(file)));
        return true;
    }
}
