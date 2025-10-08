package com.rappytv.screenshotuploader.core.ui.activity;

import com.rappytv.screenshotuploader.api.Uploader;
import com.rappytv.screenshotuploader.core.ScreenshotUploaderAddon;
import java.io.File;
import com.rappytv.screenshotuploader.core.ui.widget.UploaderWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

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
        VerticalListWidget<UploaderWidget> content = new VerticalListWidget<>().addId("content");
        content.setComparator((w1, w2) -> {
            if(!(w1 instanceof UploaderWidget u1 && w2 instanceof UploaderWidget u2)) return 0;

            return u1.getUploader().getName().compareToIgnoreCase(u2.getUploader().getName());
        });

        headerWidget.addEntry(titleWidget);

        for(Uploader<?> uploader : ScreenshotUploaderAddon.uploaderRegistry().getUploaders()) {
            content.addChild(new UploaderWidget(uploader, this.file));
        }

        windowWidget.addContent(headerWidget);
        windowWidget.addContent(new ScrollWidget(content));
        this.document.addChild(windowWidget);
    }
}
