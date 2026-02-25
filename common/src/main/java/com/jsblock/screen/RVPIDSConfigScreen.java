package com.jsblock.screen;

import com.jsblock.block.PIDSRVBase;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.screen.PIDSConfigScreen;
import mtr.screen.WidgetBetterCheckbox;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Railway Vision PIDS Configuration Screen.
 * @author LX86
 * @since 1.0.4
 */
public class RVPIDSConfigScreen extends ScreenMapper implements IGui, IPacket {

    private final BlockPos pos1;
    private final BlockPos pos2;
    private final String[] messages;
    private final boolean[] hideArrival;
    private String presetID;
    private boolean hidePlatformNumber;
    private final WidgetBetterTextField[] textFieldMessages;
    private final WidgetBetterCheckbox[] buttonsHideArrival;
    private final WidgetBetterCheckbox buttonsHidePlatformNumbers;
    private final WidgetSuggestionTextField presetIDTextField;
    private final WidgetBetterCheckbox selectAllCheckbox;
    private final Button filterButton;
    private final Set<Long> filterPlatformIds;
    private final Component messageText = Text.translatable("gui.mtr.pids_message");
    private final Component hideArrivalText = Text.translatable("gui.mtr.hide_arrival");
    private final Component hidePlatformNumberText = Text.translatable("gui.jsblock.hide_platform_number");
    private final Component presetText = Text.translatable("gui.jsblock.pids_preset");

    private static final int MAX_MESSAGE_LENGTH = 2048;

    public RVPIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals, boolean hidePlatformNumber, String presetID) {
        super(Text.literal(""));
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.hidePlatformNumber = hidePlatformNumber;
        this.presetID = presetID;
        messages = new String[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            messages[i] = "";
        }
        hideArrival = new boolean[maxArrivals];
        filterPlatformIds = new HashSet<>();

        textFieldMessages = new WidgetBetterTextField[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            textFieldMessages[i] = new WidgetBetterTextField("", MAX_MESSAGE_LENGTH);
        }

        selectAllCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.automatically_detect_nearby_platform"), checked -> {
        });

        presetIDTextField = new WidgetSuggestionTextField("None", JobanCustomResources.PIDSPresets.keySet(), MAX_MESSAGE_LENGTH, true);
        buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];
        buttonsHidePlatformNumbers = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.literal(""), checked -> {
        });
        for (int i = 0; i < maxArrivals; i++) {
            buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, hideArrivalText, checked -> {
            });
        }

        final Level world = Minecraft.getInstance().level;
        if (world != null) {
            final BlockEntity entity = world.getBlockEntity(pos1);
            if (entity instanceof PIDSRVBase.TileEntityBlockRVPIDS) {
                filterPlatformIds.addAll(((PIDSRVBase.TileEntityBlockRVPIDS) entity).getPlatformIds());
                for (int i = 0; i < maxArrivals; i++) {
                    messages[i] = ((PIDSRVBase.TileEntityBlockRVPIDS) entity).getMessage(i);
                    hideArrival[i] = ((PIDSRVBase.TileEntityBlockRVPIDS) entity).getHideArrival(i);
                }
            }
        }
        filterButton = PIDSConfigScreen.getPlatformFilterButton(pos1, selectAllCheckbox, filterPlatformIds, this);
    }

    @Override
    protected void init() {
        super.init();
        final int textWidth = font.width(hideArrivalText) + SQUARE_SIZE + TEXT_PADDING * 2;
        int startY = SQUARE_SIZE;

        IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, startY, PANEL_WIDTH);
        selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
        addDrawableChild(selectAllCheckbox);

        startY += SQUARE_SIZE;

        IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, startY, PANEL_WIDTH / 2);
        filterButton.setMessage(Text.translatable("selectWorld.edit"));
        addDrawableChild(filterButton);

        startY += SQUARE_SIZE;

        for (int i = 0; i < textFieldMessages.length; i++) {
            final WidgetBetterTextField textFieldMessage = textFieldMessages[i];
            IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, startY + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING), width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
            textFieldMessage.setValue(messages[i]);
            addDrawableChild(textFieldMessage);

            final WidgetBetterCheckbox buttonHideArrival = buttonsHideArrival[i];
            IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, startY + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING), textWidth);
            buttonHideArrival.setChecked(hideArrival[i]);
            addDrawableChild(buttonHideArrival);

            startY += SQUARE_SIZE + (TEXT_PADDING / 2);
        }

        IDrawing.setPositionAndWidth(buttonsHidePlatformNumbers, width - SQUARE_SIZE - textWidth + TEXT_PADDING, startY + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING), textWidth);
        buttonsHidePlatformNumbers.setChecked(hidePlatformNumber);
        addDrawableChild(buttonsHidePlatformNumbers);

        startY += SQUARE_SIZE + (TEXT_PADDING / 2);

        IDrawing.setPositionAndWidth(presetIDTextField, width - SQUARE_SIZE - textWidth + TEXT_PADDING, startY + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING), textWidth);
        presetIDTextField.setValue(presetID);
        addDrawableChild(presetIDTextField);
    }

    @Override
    public void tick() {
        for (final WidgetBetterTextField textFieldMessage : textFieldMessages) {
            textFieldMessage.tick();
        }
    }

    @Override
    public void onClose() {
        for (int i = 0; i < textFieldMessages.length; i++) {
            messages[i] = textFieldMessages[i].getValue();
            hideArrival[i] = buttonsHideArrival[i].selected();
        }
        if (selectAllCheckbox.selected()) {
            filterPlatformIds.clear();
        }
        hidePlatformNumber = buttonsHidePlatformNumbers.selected();
        presetID = presetIDTextField.getValue();
        PacketClient.sendRVPIDSConfigC2S(pos1, pos2, messages, hideArrival, filterPlatformIds, hidePlatformNumber, presetID);
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(guiGraphics);
            guiGraphics.drawString(font, Text.translatable("gui.mtr.filtered_platforms", selectAllCheckbox.selected() ? 0 : filterPlatformIds.size()), SQUARE_SIZE + (filterButton.getWidth() + TEXT_PADDING), (SQUARE_SIZE * 2) + (TEXT_PADDING), ARGB_WHITE);
            guiGraphics.drawString(font, messageText, SQUARE_SIZE, (SQUARE_SIZE * 3) + TEXT_PADDING, ARGB_WHITE);
            guiGraphics.drawString(font, hidePlatformNumberText, SQUARE_SIZE, ((SQUARE_SIZE + TEXT_PADDING) + (SQUARE_SIZE * 8)), ARGB_WHITE);
            guiGraphics.drawString(font, presetText, SQUARE_SIZE, (SQUARE_SIZE + TEXT_PADDING) + (SQUARE_SIZE * 9), ARGB_WHITE);

            super.render(guiGraphics, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
