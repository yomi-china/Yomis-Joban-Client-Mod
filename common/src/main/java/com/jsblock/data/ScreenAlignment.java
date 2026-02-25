package com.jsblock.data;

public enum ScreenAlignment {
    HORZ_LEFT,
    HORZ_CENTER,
    HORZ_RIGHT,
    VERT_TOP,
    VERT_CENTER,
    VERT_BOTTOM;

    public static int getX(ScreenAlignment alignment, int screenWidth, int componentWidth) {
        switch(alignment) {
            case HORZ_LEFT:
                return 0;
            case HORZ_RIGHT:
                return screenWidth - componentWidth;
            case HORZ_CENTER:
                return ((screenWidth - componentWidth)/2);
            default:
                return 0;
        }
    }

    public static int getY(ScreenAlignment alignment, int screenHeight, int componentHeight) {
        switch(alignment) {
            case VERT_TOP:
                return 0;
            case VERT_BOTTOM:
                return screenHeight - componentHeight;
            case VERT_CENTER:
                return (screenHeight / 2) - (componentHeight / 2);
            default:
                return 0;
        }
    }
}
