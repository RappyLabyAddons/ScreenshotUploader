package com.rappytv.screenshotuploader.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class ZiplineSubconfig extends Config {

    @TextFieldSetting
    private final ConfigProperty<String> base = new ConfigProperty<>("https://example.org");

    @TextFieldSetting
    private final ConfigProperty<String> auth = new ConfigProperty<>("");

    @SettingSection("settings")
    @IntroducedIn(namespace = "screenshotuploader", value = "1.0.3")
    @TextFieldSetting
    private final ConfigProperty<String> domains = new ConfigProperty<>("");

    @IntroducedIn(namespace = "screenshotuploader", value = "1.0.3")
    @DropdownSetting
    private final ConfigProperty<ZiplineNameFormat> nameFormat = new ConfigProperty<>(ZiplineNameFormat.RANDOM);

    @IntroducedIn(namespace = "screenshotuploader", value = "1.0.3")
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

    public ConfigProperty<Integer> compression() {
        return this.compression;
    }

    public enum ZiplineNameFormat {
        RANDOM,
        RANDOM_WORDS("random-words"),
        NAME,
        DATE,
        UUID;

        private final String value;

        ZiplineNameFormat() {
            this.value = this.name().toLowerCase();
        }

        ZiplineNameFormat(String value) {
            this.value = value;
        }

        public String toHeader() {
            return this.value;
        }
    }
}
