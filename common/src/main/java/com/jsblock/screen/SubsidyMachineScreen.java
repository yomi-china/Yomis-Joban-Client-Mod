package com.jsblock.screen;

import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

/**
 * Subsidy Machine Configuration Screen.
 * @author LX86
 * @since 1.1.4
 */
public class SubsidyMachineScreen extends ScreenMapper implements IGui {

    private final WidgetIntegerTextField textBoxPricePerClick;
    private final WidgetIntegerTextField textBoxTimeout;
    private final BlockPos pos;
    private int pricePerClick;
    private int timeout;

    private static final int TEXT_PADDING = 16;
    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int FINAL_TEXT_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;
    private static final int MAX_TEXT_LENGTH = 6;

    public SubsidyMachineScreen(BlockPos pos, int pricePerClick, int timeout) {
        super(Text.literal(""));
        this.pos = pos;
        this.pricePerClick = pricePerClick;
        this.timeout = timeout;

        textBoxPricePerClick = new WidgetIntegerTextField(10, true, MAX_TEXT_LENGTH);
        textBoxTimeout = new WidgetIntegerTextField(0, true, MAX_TEXT_LENGTH);
    }

    @Override
    protected void init() {
        super.init();
        int i = 1;
        IDrawing.setPositionAndWidth(textBoxPricePerClick, width - SQUARE_SIZE - TEXT_FIELD_WIDTH, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, TEXT_FIELD_WIDTH);
        IDrawing.setPositionAndWidth(textBoxTimeout, width - SQUARE_SIZE - TEXT_FIELD_WIDTH, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, TEXT_FIELD_WIDTH);

        textBoxPricePerClick.setValue(String.valueOf(pricePerClick));
        textBoxTimeout.setValue(String.valueOf(timeout));

        addDrawableChild(textBoxPricePerClick);
        addDrawableChild(textBoxTimeout);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(guiGraphics);
            guiGraphics.drawCenteredString(font, Text.translatable("block.jsblock.subsidy_machine_1"), width / 2, TEXT_PADDING, ARGB_WHITE);

            int i = 1;
            guiGraphics.drawString(font, Text.translatable("gui.jsblock.subsidyScreen.price"), SQUARE_SIZE, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, ARGB_WHITE);
            guiGraphics.drawString(font, Text.translatable("gui.jsblock.subsidyScreen.timeout"), SQUARE_SIZE, FINAL_TEXT_HEIGHT * (i++) + SQUARE_SIZE, ARGB_WHITE);
            super.render(guiGraphics, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        pricePerClick = textBoxPricePerClick.getIntegerValue(1);
        timeout = textBoxTimeout.getIntegerValue(0);

        PacketClient.sendSubsidyConfigC2S(pos, pricePerClick, timeout);
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
