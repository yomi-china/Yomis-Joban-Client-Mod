package com.jsblock.screen;

import mtr.screen.WidgetBetterTextField;

/**
 * An integer only text field, that does the dirty work of try catching so I don't have to worry about it.
 * @author LX86
 * @since 1.1.5
 * @see WidgetBetterTextField
 */
public class WidgetIntegerTextField extends WidgetBetterTextField {
    private final int defaultValue;

    public WidgetIntegerTextField(int defaultValue, boolean positiveOnly, int maxLength) {
        super(positiveOnly ? TextFieldFilter.POSITIVE_INTEGER : TextFieldFilter.INTEGER, String.valueOf(defaultValue), maxLength);
        this.defaultValue = defaultValue;
    }

    public int getIntegerValue(int minValue) {
        try {
            return Math.max(minValue, Integer.parseInt(getValue()));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getIntegerValue() {
        return getIntegerValue(Integer.MIN_VALUE);
    }
}
