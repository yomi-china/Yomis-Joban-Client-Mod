package com.jsblock.data;

import net.minecraft.network.chat.Component;

public class TextLabel {
    public Component text;
    public ScreenAlignment horizontalAlignment;
    public double y;
    public float scale;

    public TextLabel(Component text, float scale, ScreenAlignment horizontalAlignment, int y) {
        this.text = text;
        this.scale = scale;
        this.horizontalAlignment = horizontalAlignment;
        this.y = y;
    }
}
