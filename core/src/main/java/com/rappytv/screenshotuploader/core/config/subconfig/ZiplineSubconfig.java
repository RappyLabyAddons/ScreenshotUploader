package com.rappytv.screenshotuploader.core.config.subconfig;

import com.rappytv.screenshotuploader.core.util.ZiplineNameFormat;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.Debounce;

public class ZiplineSubconfig extends Config {

    public ZiplineSubconfig() {
        this.base.addChangeListener((value) -> Debounce.of("uploader-zipline-base", 2000, () -> {
            if(!value.startsWith("http://") && !value.startsWith("https://")) {
                Laby.labyAPI().notificationController().push(
                    Notification.builder()
                        .title(Component.translatable("screenshotuploader.toast.error"))
                        .text(Component.translatable("screenshotuploader.toast.http"))
                        .build()
                );
            } else if(value.endsWith("/")) {
                Laby.labyAPI().notificationController().push(
                    Notification.builder()
                        .title(Component.translatable("screenshotuploader.toast.error"))
                        .text(Component.translatable("screenshotuploader.toast.slash"))
                        .build()
                );
            }
        }));
    }

    @TextFieldSetting
    private final ConfigProperty<String> base = new ConfigProperty<>("https://example.org");

    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    @SettingSection("settings")
    @IntroducedIn(namespace = "uploader", value = "1.0.3")
    @TextFieldSetting
    private final ConfigProperty<String> domains = new ConfigProperty<>("");

    @IntroducedIn(namespace = "uploader", value = "1.0.3")
    @SettingRequires(value = "zeroWidth", invert = true)
    @DropdownSetting
    private final ConfigProperty<ZiplineNameFormat> nameFormat = new ConfigProperty<>(ZiplineNameFormat.DEFAULT);

    @IntroducedIn(namespace = "uploader", value = "1.0.3")
    @SwitchSetting
    private final ConfigProperty<Boolean> zeroWidth = new ConfigProperty<>(false);

    @IntroducedIn(namespace = "uploader", value = "1.0.3")
    @SwitchSetting
    private final ConfigProperty<Boolean> embed = new ConfigProperty<>(true);

    @IntroducedIn(namespace = "uploader", value = "1.0.3")
    @SliderSetting(min = 0, max = 100, steps = 10f)
    private final ConfigProperty<Integer> compression = new ConfigProperty<>(0);

    public ConfigProperty<String> base() {
        return this.base;
    }

    public ConfigProperty<String> auth() {
        return this.auth;
    }

    public ConfigProperty<String> domains() {
        return this.domains;
    }

    public ConfigProperty<ZiplineNameFormat> nameFormat() {
        return this.nameFormat;
    }

    public ConfigProperty<Boolean> zeroWidth() {
        return this.zeroWidth;
    }

    public ConfigProperty<Boolean> embed() {
        return this.embed;
    }

    public ConfigProperty<Integer> compression() {
        return this.compression;
    }
}
