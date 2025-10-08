package com.rappytv.screenshotuploader.core.ui.widget;

import com.rappytv.screenshotuploader.api.UploadException;
import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.concurrent.task.Task;
import java.io.File;

@AutoWidget
public class UploaderWidget extends HorizontalListWidget {

    private final Uploader<?> uploader;
    private final File file;

    public UploaderWidget(Uploader<?> uploader, File file) {
        this.uploader = uploader;
        this.file = file;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        IconWidget icon = new IconWidget(this.uploader.getIcon()).addId("icon");
        ComponentWidget name = ComponentWidget.text(this.uploader.getName()).addId("name");
        ButtonWidget button = ButtonWidget.i18n("screenshotuploader.activity.upload").addId("button");

        if(!this.uploader.validateConfig()) {
            button.setEnabled(false);
            button.setHoverComponent(Component.translatable(
                "screenshotuploader.activity.invalidConfig",
                NamedTextColor.RED
            ));
        }

        button.setActionListener(() -> {
            button.setEnabled(false);
            button.updateComponent(Component.translatable("screenshotuploader.activity.uploading", NamedTextColor.AQUA));
            Task.builder(() -> {
                try {
                    String url = this.uploader.uploadScreenshot(this.file);

                    Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                        button.setEnabled(true);
                        button.updateComponent(Component.translatable("screenshotuploader.activity.copy", NamedTextColor.GREEN));
                    });
                    Laby.labyAPI().notificationController().push(
                        Notification.builder()
                            .title(Component.translatable("screenshotuploader.notification.success"))
                            .text(Component.translatable("screenshotuploader.activity.uploaded", Component.text(this.uploader.getName())))
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

        this.addEntry(icon);
        this.addEntry(name);
        this.addEntry(button);
    }

    public Uploader<?> getUploader() {
        return this.uploader;
    }

    @Override
    public int getSortingValue() {
        return 1;
    }
}
