package com.jsblock.data;

public class ScreenRoot {
    public ScreenAlignment screenAlignment;
    public int width;
    public int height;
    public int startX;
    public float widthFactor;
    public float heightFactor;

    public ScreenRoot(ScreenAlignment screenAlignment, float widthFactor, float heightFactor) {
        this.screenAlignment = screenAlignment;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
    }

    public void init(float screenWidth, float screenHeight) {
        width = (int) (screenWidth / widthFactor);
        height = (int) (screenHeight / heightFactor);
        startX = getXPadding(width, screenWidth);
    }

    public static int getXPadding(float screenWidth, float totalWidth) {
        return (int)(totalWidth - screenWidth) / 2;
    }
}
