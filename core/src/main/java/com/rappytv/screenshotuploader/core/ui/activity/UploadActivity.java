package com.rappytv.screenshotuploader.core.ui.activity;

import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import java.io.File;
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
import net.labymod.api.util.concurrent.task.Task;

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
        ComponentWidget titleWidget = ComponentWidget.i18n("screenshotuploader.activity.title").addId("title");
        VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");

        headerWidget.addEntry(titleWidget);

        for(Uploader<?> uploader : ScreenshotUploaderAddon.uploaderRegistry().getUploaders()) {
            HorizontalListWidget uploaderWidget = new HorizontalListWidget().addId("uploader");
            IconWidget icon = new IconWidget(uploader.getIcon()).addId("icon");
            ComponentWidget name = ComponentWidget.text(uploader.getName()).addId("name");
            ButtonWidget button = new ButtonWidget().addId("button");
            if(!uploader.validateConfig()) {
                button.setEnabled(false);
                button.updateComponent(Component.translatable("screenshotuploader.activity.invalidConfig", NamedTextColor.RED));
            } else button.updateComponent(Component.translatable("screenshotuploader.activity.upload"));

            button.setActionListener(() -> {
                button.setEnabled(false);
                button.updateComponent(Component.translatable("screenshotuploader.activity.uploading", NamedTextColor.AQUA));
                Task.builder(() -> {
                    try {
                        String url = uploader.uploadScreenshot(this.file);

                        Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                            button.setEnabled(true);
                            button.updateComponent(Component.translatable("screenshotuploader.activity.copy", NamedTextColor.GREEN));
                        });
                        Laby.labyAPI().notificationController().push(
                            Notification.builder()
                                .title(Component.translatable("screenshotuploader.notification.success"))
                                .text(Component.translatable("screenshotuploader.activity.uploaded", Component.text(uploader.getName())))
                                .build()
                        );
                        button.setActionListener(() -> {
                            Laby.labyAPI().notificationController().push(
                                Notification.builder()
                                    .title(Component.translatable("screenshotuploader.notification.success"))
                                    .text(Component.translatable("screenshotuploader.activity.copied"))
                                    .build()
                            );
                            if(!url.isBlank()) {
                                Laby.labyAPI().minecraft().chatExecutor().copyToClipboard(url);
                            }
                        });
                    } catch (UploadException e) {
                        ScreenshotUploaderAddon.logging().error("Failed to upload screenshot to " + e.getUploader().getName(), e);
                        Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                            button.setEnabled(true);
                            button.updateComponent(Component.translatable("screenshotuploader.activity.error", NamedTextColor.RED));
                        });
                        Laby.labyAPI().notificationController().push(
                            Notification.builder()
                                .title(Component.translatable("screenshotuploader.notification.error"))
                                .text(Component.translatable("screenshotuploader.activity.checkTheLog"))
                                .build()
                        );
                    }
                }).build().execute();
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
