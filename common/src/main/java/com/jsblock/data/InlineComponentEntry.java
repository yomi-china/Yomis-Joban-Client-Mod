package com.jsblock.data;

import net.minecraft.client.gui.components.AbstractWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineComponentEntry {
    public int availableWidth;
    public ScreenAlignment horizontalAlignment;
    public ScreenAlignment verticalAlignment;
    public final static int MARGIN = 10;
    public List<AbstractWidget> widgetList;

    public InlineComponentEntry(ScreenAlignment horizontalAlignment, ScreenAlignment verticalAlignment, AbstractWidget... widget) {
        widgetList = new ArrayList<>();
        widgetList.addAll(Arrays.asList(widget));
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    public void setAvailableWidth(int availableWidth) {
        this.availableWidth = availableWidth;
    }

    public int calculateWidth() {
        return (availableWidth / widgetList.size()) - MARGIN;
    }
}
