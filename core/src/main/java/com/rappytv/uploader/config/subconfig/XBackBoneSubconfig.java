package com.rappytv.uploader.config.subconfig;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.Debounce;

public class XBackBoneSubconfig extends Config {

    public XBackBoneSubconfig() {
        base.addChangeListener((value) -> Debounce.of("uploader-xbackbone-base", 2000, () -> {
            if(!value.startsWith("http://") && !value.startsWith("https://")) {
                Laby.labyAPI().notificationController().push(
                    Notification.builder()
                        .title(Component.translatable("uploader.toast.error"))
                        .text(Component.translatable("uploader.toast.http"))
                        .build()
                );
            } else if(value.endsWith("/")) {
                Laby.labyAPI().notificationController().push(
                    Notification.builder()
                        .title(Component.translatable("uploader.toast.error"))
                        .text(Component.translatable("uploader.toast.slash"))
                        .build()
                );
            }
        }));
    }

    @TextFieldSetting
    private final ConfigProperty<String> base = new ConfigProperty<>("https://example.org");
    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    public String base() {
        return base.get();
    }
    public String auth() {
        return auth.get();
    }
}
