package com.jsblock.screen;

import com.jsblock.Joban;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.Mth;

/**
 * The GUI for Incompatible MTR Versions
 * @author LX86
 * @since 1.1.6
 */
public class IncompatibleVersionScreen extends ScreenMapper implements IGui {

    private final Button ignoreButton;
    private final String minVersion;
    private final String currentVersion;
    public static final int BUTTON_HEIGHT = TEXT_HEIGHT + 12;
    private static final int TEXT_PADDING = 16;
    private static final int FINAL_TEXT_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public IncompatibleVersionScreen(String minVersion, String currentVersion) {
        super(Text.literal(""));
        this.minVersion = minVersion;
        this.currentVersion = currentVersion;
        ignoreButton = UtilitiesClient.newButton(Text.literal(""), btn -> {
            onClose();
            UtilitiesClient.setScreen(minecraft, null);
        });
        ignoreButton.setMessage(Text.translatable("gui.jsblock.ignore"));
    }

    @Override
    protected void init() {
        super.init();
        int btnWidth = Mth.clamp((int)(width / 1.25), 0, 380);
        IDrawing.setPositionAndWidth(ignoreButton, ((width - btnWidth) / 2), height - SQUARE_SIZE, btnWidth);
        addDrawableChild(ignoreButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(guiGraphics);
            guiGraphics.drawCenteredString(font, Text.translatable("gui.jsblock.incompatible_title").withStyle(ChatFormatting.RED), width / 2, TEXT_PADDING, ARGB_WHITE);

            int i = 1;
            guiGraphics.drawCenteredString(font, Text.translatable("gui.jsblock.incompatible_1", Joban.getVersion(), currentVersion), width / 2, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, ARGB_WHITE);
            guiGraphics.drawCenteredString(font, Text.translatable("gui.jsblock.incompatible_2", minVersion), width / 2, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, ARGB_WHITE);
            guiGraphics.drawCenteredString(font, Text.translatable("gui.jsblock.incompatible_3"), width / 2, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, ARGB_WHITE);

            super.render(guiGraphics, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
