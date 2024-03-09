package com.rappytv.uploader.config.subconfig;

import com.rappytv.uploader.util.ZiplineNameFormat;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.Debounce;

public class ZiplineSubconfig extends Config {

    public ZiplineSubconfig() {
        base.addChangeListener((value) -> Debounce.of("uploader-zipline-base", 2000, () -> {
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
    @SettingSection("settings")
    @TextFieldSetting
    private final ConfigProperty<String> domains = new ConfigProperty<>("");
    @SettingRequires(value = "zeroWidth", invert = true)
    @DropdownSetting
    private final ConfigProperty<ZiplineNameFormat> nameFormat = new ConfigProperty<>(ZiplineNameFormat.DEFAULT);
    @SwitchSetting
    private final ConfigProperty<Boolean> zeroWidth = new ConfigProperty<>(false);
    @SwitchSetting
    private final ConfigProperty<Boolean> embed = new ConfigProperty<>(true);
    @SliderSetting(min = 0, max = 100, steps = 10f)
    private final ConfigProperty<Integer> compression = new ConfigProperty<>(0);

    public String base() {
        return base.get();
    }
    public String auth() {
        return auth.get();
    }
    public String domains() {
        return domains.get();
    }
    public ZiplineNameFormat nameFormat() {
        return nameFormat.get();
    }
    public boolean zeroWidth() {
        return zeroWidth.get();
    }
    public boolean embed() {
        return embed.get();
    }
    public int compression() {
        return compression.get();
    }
}
