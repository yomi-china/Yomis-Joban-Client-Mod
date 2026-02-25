package com.jsblock.data;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.MutableComponent;

/**
 * Representing an entry in the Config Screen
 */
public class ConfigGuiEntry {
    public MutableComponent text;
    public AbstractWidget widget;
    public int y;
    public int widgetWidth;
    public int widgetHeight;

    public ConfigGuiEntry(MutableComponent text, AbstractWidget widget, int widgetWidth, int widgetHeight) {
        this.text = text;
        this.widget = widget;
        this.widgetHeight = widgetHeight;
        this.widgetWidth = widgetWidth;
    }
}
