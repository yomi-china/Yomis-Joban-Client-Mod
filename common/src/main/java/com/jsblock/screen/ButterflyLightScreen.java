package com.jsblock.screen;

import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

/**
 * The GUI for Fare Saver
 * @author LX86
 * @since 1.1.4
 */
public class ButterflyLightScreen extends ScreenMapper implements IGui {

    private final WidgetIntegerTextField textBoxCountdown;
    private final BlockPos pos;
    private int countdown;

    private static final int DEFAULT_DISCOUNT = 2;
    private static final int MAX_TEXT_LENGTH = 4;
    private static final int TEXT_PADDING = 16;
    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int FINAL_TEXT_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public ButterflyLightScreen(BlockPos pos, int countdown) {
        super(Text.literal(""));
        this.pos = pos;
        this.countdown = countdown;

        textBoxCountdown = new WidgetIntegerTextField(DEFAULT_DISCOUNT, true, MAX_TEXT_LENGTH);
    }

    @Override
    protected void init() {
        super.init();
        int i = 1;
        IDrawing.setPositionAndWidth(textBoxCountdown, width - SQUARE_SIZE - TEXT_FIELD_WIDTH, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, TEXT_FIELD_WIDTH);
        textBoxCountdown.setValue(String.valueOf(countdown));
        addDrawableChild(textBoxCountdown);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(guiGraphics);
            guiGraphics.drawCenteredString(font, Text.translatable("block.jsblock.butterfly_light"), width / 2, TEXT_PADDING, ARGB_WHITE);

            int i = 1;
            guiGraphics.drawString(font, Text.translatable("gui.jsblock.butterfly.countdown"), SQUARE_SIZE, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, ARGB_WHITE);
            super.render(guiGraphics, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        countdown = textBoxCountdown.getIntegerValue(1);
        PacketClient.sendButterflyConfigC2S(pos, countdown);
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
