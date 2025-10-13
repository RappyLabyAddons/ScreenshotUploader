# Screenshot Uploader

To use the ScreenshotUploader API, you first need to add the addon as a **required dependency**.
Open your `build.gradle.kts` and include `addon("screenshotuploader")` inside your `labyMod.addonInfo` block.

All API module classes include Javadocs that explain most functionality. These comments are stripped during compilation but remain visible on <a href="https://github.com/RappyLabyAddons/ScreenshotUploader/tree/master/api/src/main/java/com/rappytv/screenshotuploader/api" target="_blank">GitHub</a>.

## Creating your custom `Uploader`

To implement your own uploader, create a class that extends the abstract `Uploader` class from the ScreenshotUploader API.

If you're not using a custom configuration yet, you can pass `EmptyConfig` as the generic type, return `EMPTY_CONFIG` in `Uploader#getConfig`, and return `true` in `Uploader#validateConfig`.
Refer to the [Configuration section](#add-config-options-for-your-custom-uploader) for more information on using custom configs.

The `Uploader` constructor requires two strings:

1. **Uploader ID** - a unique identifier used internally to distinguish uploaders.
2. **Display Name** - the user-friendly name shown in the upload menu and in success/error messages.

You should also override `Uploader#getIcon` to specify the icon displayed next to the display name in the uploader selection menu.

Next comes the core logic - implementing `Uploader#uploadFile`.
This method runs **asynchronously**, so there's no need to manually manage threads, futures, or async requests. It must return a `String` - the **URL** of the uploaded screenshot.

While you can use any HTTP request system, it's recommended to use **LabyMod's built-in Request/Response system**, as it's simple and reliable.
If your upload fails (e.g., due to an API error or exception), throw a new `UploadException`, passing `this` as the second parameter to indicate which uploader failed.

## Add config options for your `Uploader`

The ScreenshotUploader API supports any configuration class that extends LabyMod's `Config` class.
This allows you to integrate uploader-specific settings seamlessly. However, note that you must still display your config within your addon's configuration UI - the `Uploader#getConfig` method simply serves as a shortcut to easily access your config instance.

To ensure user configuration values are valid, override `Uploader#validateConfig`.
This method runs when the uploader menu opens and determines whether the upload button is enabled or not.
This can be used to validate if an API key was actually pasted into the settings or not.

## Registering your `Uploader`

You can register your custom uploader using `UploaderRegistry#registerUploader`, accessible through `ScreenshotUploaderAddon#uploaderRegistry`.

## Example `Uploader`

Below is a full working example summarizing everything discussed above:

=== ":octicons-file-code-16: CustomUploader"
    ```java
    public class CustomUploader extends Uploader<YourAddonConfig> {

        private static final String UPLOAD_ENDPOINT = "https://your.uploader.domain/endpoint";
        private static final Icon ICON = Icon.texture(/* a resource location to your desired icon */);
    
        private final YourAddon addon;
    
        public ImgurUploader(YourAddon addon) {
            super("custom", "Custom");
            this.addon = addon;
        }
    
        @Override
        public Icon getIcon() {
            return ICON;
        }
    
        @Override
        public @NotNull YourAddonConfig getConfig() {
            return this.addon.configuration();
        }
    
        @Override
        public boolean validateConfig() {
            return !this.getConfig().auth().isBlank();
        }
    
        @Override
        public String uploadScreenshot(File file) throws UploadException {
            FormData formData;
    
            try {
                formData = FormData.builder()
                    .name("image")
                    .fileName(file.getName())
                    .contentType("image/png")
                    .value(file.toPath())
                    .build();
            } catch (IOException e) {
                throw new UploadException(e, this);
            }
    
            Response<JsonObject> response = Request.ofGson(JsonObject.class)
                .url(UPLOAD_ENDPOINT)
                .method(Method.POST)
                .form(formData)
                .addHeader("Authorization", this.getConfig().auth().get())
                .handleErrorStream()
                .executeSync();
    
            if (response.hasException()) {
                throw new UploadException(response.exception(), this);
            }
    
            try {
                int statusCode = response.getStatusCode();
                JsonObject body = response.get();
    
                if (statusCode != 200) {
                    if (body != null && body.has("error")) {
                        throw new UploadException(body.get("error").getAsString(), this);
                    }
    
                    throw new UploadException("Failed to upload file with status " + statusCode, this);
                }
    
                if (body != null && body.has("link")) {
                    return body.get("link").getAsString();
                }
    
                throw new UploadException("Response body does not contain the file link", this);
            } catch (IllegalArgumentException e) {
                throw new UploadException(e, this);
            }
        }
    }
    ```

=== ":octicons-file-code-16: YourAddonConfig"
    ```java
    public class YourAddonConfig extends AddonConfig {

        @SwitchSetting
        private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    
        @TextFieldSetting
        private final ConfigProperty<String> auth = new ConfigProperty<>("");
    
        @Override
        public ConfigProperty<Boolean> enabled() {
          return this.enabled;
        }
    
        public ConfigProperty<String> auth() {
          return this.auth;
        }
    }
    ```

=== ":octicons-file-code-16: YourAddon"
    ```java
    public class YourAddon extends LabyAddon<YourAddonConfig> {

        @Override
        protected void enable() {
          this.registerSettingCategory();
          ScreenshotUploaderAddon.uploaderRegistry().registerUploader(new CustomUploader(this));
        }
    }
    ```
=== ":octicons-file-code-16: build.gradle.kts"
    ```kotlin
    labyMod {
        addonInfo {
            addon("screenshotuploader") // (1)
        }
    }
    ```

    1. Don't remove other code from your `build.gradle.kts` file - this snippet only shows where to place the addon dependency.

### Production Examples

Here are some real-world implementations of custom uploaders built using the ScreenshotUploader API:

- <a href="https://craftshot.net" target="_blank">CraftShot.net</a>: <a href="https://github.com/RappyLabyAddons/ScreenshotUploader/blob/master/core/src/main/java/com/rappytv/screenshotuploader/core/uploader/uploaders/CraftShotUploader.java" target="_blank">CraftShotUploader.java</a>
- <a href="https://imgur.com" target="_blank">Imgur</a>: <a href="https://github.com/RappyLabyAddons/ScreenshotUploader/blob/master/core/src/main/java/com/rappytv/screenshotuploader/core/uploader/uploaders/ImgurUploader.java" target="_blank">ImgurUploader.java</a>
- <a href="https://xbackbone.app" target="_blank">XBackBone</a>: <a href="https://github.com/RappyLabyAddons/ScreenshotUploader/blob/master/core/src/main/java/com/rappytv/screenshotuploader/core/uploader/uploaders/XBackBoneUploader.java" target="_blank">XBackBoneUploader.java</a>
- <a href="https://zipline.diced.sh" target="_blank">Zipline</a>: <a href="https://github.com/RappyLabyAddons/ScreenshotUploader/blob/master/core/src/main/java/com/rappytv/screenshotuploader/core/uploader/uploaders/ZiplineUploader.java" target="_blank">ZiplineUploader.java</a>