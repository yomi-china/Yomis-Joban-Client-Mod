package com.jsblock.screen;

import mtr.mappings.UtilitiesClient;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This text field just suggest stuff, autofill when enter is pressed. <br>
 * Also make it red when it can't find any suggestion
 * @author LX86
 * @since 1.1.4
 * @see WidgetBetterTextField
 */
public class WidgetSuggestionTextField extends WidgetBetterTextField {
    private final Collection<String> suggestionList;
    private List<String> matchedSuggestionList;
    private final int RED_COLOR = 16733525;
    private final int WHITE_COLOR = 16777215;
    private String currentSuggestion = "";

    public WidgetSuggestionTextField(String defaultSuggestion, Collection<String> suggestionList, int maxLength, boolean strict) {
        super(defaultSuggestion, maxLength);
        this.suggestionList = suggestionList;
        this.matchedSuggestionList = new ArrayList<>(suggestionList);
    }

    @Override
    public void setResponder(Consumer<String> changedListener) {
        super.setResponder(text -> {
            matchedSuggestionList = text.isEmpty() ? new ArrayList<>(suggestionList) : suggestionList.stream().filter(str -> str.startsWith(text)).collect(Collectors.toList());

            if(matchedSuggestionList.isEmpty()) {
                this.setTextColor(RED_COLOR);
            } else {
                this.setTextColor(WHITE_COLOR);
            }

            if(!text.isEmpty() && !matchedSuggestionList.isEmpty()) {
                setSuggestion(matchedSuggestionList.get(0).substring(text.length()));
                currentSuggestion = matchedSuggestionList.get(0);
            }
            changedListener.accept(text);
        });
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (this.canConsumeInput() && !this.getValue().isEmpty()) {
            /* 257 / 335 = Enter */
            if(i == 257 || i == 335) {
                this.setValue(currentSuggestion);
            }
        }
        return super.keyPressed(i, j, k);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if(isFocused()) {
            Font font = Minecraft.getInstance().font;

            int i = 0;

            for(String suggestion : matchedSuggestionList) {
                int color = i == 0 ? ChatFormatting.YELLOW.getColor() : ARGB_WHITE;
                guiGraphics.drawString(font, suggestion, UtilitiesClient.getWidgetX(this), (i * font.lineHeight) + (UtilitiesClient.getWidgetY(this) + height + TEXT_FIELD_PADDING), color, false);
                i++;
            }
        }
    }
}
