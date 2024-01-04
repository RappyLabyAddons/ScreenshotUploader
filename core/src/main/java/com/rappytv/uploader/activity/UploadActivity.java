package com.rappytv.uploader.activity;

import com.rappytv.uploader.api.ApiRequest;
import com.rappytv.uploader.api.Uploader;
import com.rappytv.uploader.api.Uploaders;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.notification.Notification;
import java.io.File;

@Link("upload.lss")
@AutoActivity
public class UploadActivity extends SimpleActivity {

    private final File file;

    public UploadActivity(File file) {
        this.file = file;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
        HorizontalListWidget headerWidget = new HorizontalListWidget().addId("header");
        ComponentWidget titleWidget = ComponentWidget.text("Upload Screenshot").addId("title");
        VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");

        headerWidget.addEntry(titleWidget);

        for(Uploaders uploaderId : Uploaders.values()) {
            Uploader uploader = uploaderId.getUploader();

            HorizontalListWidget uploaderWidget = new HorizontalListWidget().addId("uploader");
            IconWidget icon = new IconWidget(uploader.getIcon()).addId("icon");
            ComponentWidget name = ComponentWidget.text(uploader.getName()).addId("name");
            ButtonWidget button = new ButtonWidget()
                .updateComponent(Component.text("Upload"))
                .addId("button");

            button.setActionListener(() -> {
                button.setEnabled(false);
                button.updateComponent(Component.text("Uploading", NamedTextColor.AQUA));
                ApiRequest request = new ApiRequest(uploader, file);
                request.sendAsyncRequest().thenAccept((result) -> {
                    if(request.isSuccessful()) {
                        button.setEnabled(true);
                        button.updateComponent(Component.text("Copy", NamedTextColor.GREEN));
                        Laby.labyAPI().notificationController().push(
                            Notification.builder()
                                .title(Component.text("Success!"))
                                .text(Component.text("Your screenshot was successfully uploaded to " + uploader.getName() + "!"))
                                .build()
                        );
                        button.setActionListener(() -> {
                            Laby.labyAPI().notificationController().push(
                                Notification.builder()
                                    .title(Component.text("Success!"))
                                    .text(Component.text("The link was copied to your clipboard!"))
                                    .build()
                            );
                            Laby.labyAPI().minecraft().chatExecutor().copyToClipboard(!request.getUploadLink().isBlank() ? request.getUploadLink() : "");
                        });
                    } else {
                        button.setEnabled(true);
                        button.updateComponent(Component.text("Error", NamedTextColor.RED));
                        Laby.labyAPI().notificationController().push(
                            Notification.builder()
                                .title(Component.text("An error ocurred!"))
                                .text(Component.text(request.getError()))
                                .build()
                        );
                    }
                }).exceptionally((e) -> {
                    button.setEnabled(true);
                    button.updateComponent(Component.text("Error", NamedTextColor.RED));
                    Laby.labyAPI().notificationController().push(
                        Notification.builder()
                            .title(Component.text("An error ocurred!"))
                            .text(Component.text(e.getMessage()))
                            .build()
                    );
                    return null;
                });
            });

            uploaderWidget.addEntry(icon);
            uploaderWidget.addEntry(name);
            uploaderWidget.addEntry(button);
            content.addChild(uploaderWidget);
        }

        windowWidget.addContent(headerWidget);
        windowWidget.addContent(content);
        this.document.addChild(windowWidget);
    }
}
