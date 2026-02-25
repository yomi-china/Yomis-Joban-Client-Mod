package com.jsblock.screen;

import com.jsblock.data.ConfigGuiEntry;
import com.jsblock.data.InlineComponentEntry;
import com.jsblock.data.ScreenAlignment;
import com.jsblock.data.ScreenRoot;
import com.jsblock.data.TextLabel;
import com.jsblock.vermappings.render.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * The base configuration screen.
 * @author LX86
 * @since 1.1.4
 */
public class ConfigScreenBase extends ScreenMapper implements IGui {

	private double elapsedTime;
	private final List<InlineComponentEntry> inlineComponentList;
	private final List<ConfigGuiEntry> configList;
	private final List<TextLabel> customText;
	private final ScreenRoot screenRoot;
	private boolean initalized;
	public static final int BUTTON_HEIGHT = TEXT_HEIGHT + 12;
	private static final int TRANSITION_DURATION = 20;
	private static final int TEXT_PADDING = 12;
	private static final int CONFIG_BUTTON_WIDTH = 60;
	private static final int TEXT_FIELD_WIDTH = 100;
	private static final int FINAL_TEXT_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;
	private static final int MAX_TEXT_LENGTH = 128;
	private static final ResourceLocation BACKGROUND = new ResourceLocation("jsblock:textures/gui/background/bg.png");
	private static final ResourceLocation STAR_BACKGROUND = new ResourceLocation("jsblock:textures/gui/background/stars.png");
	private static final ResourceLocation TERRAIN_BACKGROUND = new ResourceLocation("jsblock:textures/gui/background/terrain.png");

	public ConfigScreenBase(ScreenRoot root, TextLabel... customText) {
		super(Text.literal(""));
		this.screenRoot = root;
		this.inlineComponentList = new ArrayList<>();
		this.customText = new ArrayList<>();
		this.configList = new ArrayList<>();
		this.customText.addAll(Arrays.asList(customText));
	}

	/**
	 * Register a config entry with a button as its value
	 * @param description The config entry's description
	 * @param defaultMessage The default text that appears on the button
	 * @param onClick Callback when the button is clicked
	 * @return AbstractWidget
	 */
	public AbstractWidget registerConfigRowButton(MutableComponent description, Component defaultMessage, Consumer<AbstractWidget> onClick) {
		Button button = UtilitiesClient.newButton( Text.literal(""), onClick::accept);

		button.setMessage(defaultMessage);

		configList.add(new ConfigGuiEntry(description, button, CONFIG_BUTTON_WIDTH, BUTTON_HEIGHT));
		return button;
	}

	public void registerInlineRow(InlineComponentEntry... entries) {
		inlineComponentList.addAll(Arrays.asList(entries));
	}

	@Override
	protected void init() {
		super.init();
		int i = 0;
		int startY = 0;

		for(TextLabel component : customText) {
			startY += (int)(component.y * component.scale);
		}

		screenRoot.init(width, height);

		List<ConfigGuiEntry> originalList = new ArrayList<>(configList);

		configList.clear();

		/* Config Entry List */
		for (ConfigGuiEntry entry : originalList) {
			entry.y = startY + TEXT_PADDING + (FINAL_TEXT_HEIGHT * i++ + SQUARE_SIZE);
			configList.add(entry);

			IDrawing.setPositionAndWidth(entry.widget, (width - screenRoot.startX) - CONFIG_BUTTON_WIDTH, entry.y, entry.widgetWidth);
			addDrawableChild(entry.widget);
		}

		/* Draw Inline Components */
		for(int k = 0; k < inlineComponentList.size(); k++) {
			InlineComponentEntry entry = inlineComponentList.get(k);
			entry.setAvailableWidth(screenRoot.width);
			for (int j = 0; j < entry.widgetList.size(); j++) {
				AbstractWidget widget = entry.widgetList.get(j);
				UtilitiesClient.setWidgetX(widget, entry.calculateWidth() * j);
				addDrawableChild(widget);
			}

			inlineComponentList.set(k, entry);
		}

		if(!initalized) {
			initalized = true;
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		try {
			elapsedTime += delta;

			int startY = 0;
			renderBG(guiGraphics);

			/* Render Text Label (Used to display Title and Version) */
			for(TextLabel component : customText) {
				int textWidth = (int) (font.width(component.text) * component.scale);
				int componentX = screenRoot.startX + ScreenAlignment.getX(component.horizontalAlignment, screenRoot.width, textWidth);
				int componentY = (int) (startY + component.y);

				guiGraphics.pose().pushPose();
				guiGraphics.pose().translate(componentX, componentY, 0);
				guiGraphics.pose().scale(component.scale, component.scale, component.scale);
				guiGraphics.drawString(font, component.text, 0, 0, ARGB_WHITE);

				startY += TEXT_PADDING * component.scale;
				guiGraphics.pose().popPose();
			}

			/* Draw Config Entry Description */
			for (ConfigGuiEntry entry : configList) {
				boolean mouseXInScreenRoot = RailwayData.isBetween(mouseX, screenRoot.startX, screenRoot.startX + screenRoot.width);
				boolean mouseYInThisEntry = RailwayData.isBetween(mouseY, entry.y, entry.y + entry.widgetHeight);
				if(mouseXInScreenRoot && mouseYInThisEntry) {
					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					RenderHelper.disableTexture();
					RenderHelper.setShaderColor(1.0F, 1.0F, 1.0F, 0.3F);
					guiGraphics.fill(screenRoot.startX, entry.y, screenRoot.width, entry.widgetHeight, ARGB_WHITE);
					RenderHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					RenderHelper.enableTexture();
				}

				guiGraphics.drawString(font, entry.text, screenRoot.startX, entry.y + (entry.widgetHeight / 4), ARGB_WHITE);
			}

			/* Draw inline components (Button etc.) */
			int inlineRow = 1;
			int bottomPadding = SQUARE_SIZE;
			for(InlineComponentEntry entry : inlineComponentList) {
				int startInlineY = ScreenAlignment.getY(entry.verticalAlignment, height, BUTTON_HEIGHT) - (BUTTON_HEIGHT * inlineComponentList.size()) - bottomPadding;
				int y = startInlineY + (BUTTON_HEIGHT * inlineRow);

				int totalWidth = (entry.calculateWidth() * entry.widgetList.size()) + (InlineComponentEntry.MARGIN * entry.widgetList.size());
				int startX = ScreenAlignment.getX(entry.horizontalAlignment, width, totalWidth);

				for(int i = 0; i < entry.widgetList.size(); i++) {
					AbstractWidget widget = entry.widgetList.get(i);
					int buttonX = startX + (entry.calculateWidth() * i) + (InlineComponentEntry.MARGIN * i);
					IDrawing.setPositionAndWidth(widget, buttonX, y, entry.calculateWidth());
				}
				inlineRow++;
			}
			super.render(guiGraphics, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Save and close the screen when pressing Esc */
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifier) {
		if (keyCode == 256) {
			closeScreen(true);
			return true;
		} else {
			return super.keyPressed(keyCode, scanCode, modifier);
		}
	}

	private void renderBG(GuiGraphics guiGraphics) {
		PoseStack matrices = guiGraphics.pose();
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		double easing = easeOutAnimation(Math.min(1, elapsedTime / TRANSITION_DURATION));

		/* Background */
		renderTexture(matrices, bufferBuilder, BACKGROUND, width, height, 1, 1);
		tesselator.end();

		/* Stars */
		int starBackgroundSize = Math.max(width, height) * 4;
		float starUVSize = starBackgroundSize / 384F;

		matrices.pushPose();
		matrices.translate(0, (1 - easing) * (height * 0.2), 0);
		matrices.translate(width / 2.0, height / 2.0, 0);

		float rotationAngle = ((float)elapsedTime * -0.075F) % 360;
		matrices.mulPose(new Quaternionf(0, 0, 1, rotationAngle / 360));
		matrices.translate(-width / 2.0, -height / 2.0, 0);

		renderTexture(matrices, bufferBuilder, STAR_BACKGROUND, starBackgroundSize, starBackgroundSize, starUVSize, starUVSize);
		matrices.popPose();
		tesselator.end();

		/* Terrain */
		int terrainHeight = width / 4;
		matrices.pushPose();
		matrices.translate(0, height - (terrainHeight * easing), 0);
		renderTexture(matrices, bufferBuilder, TERRAIN_BACKGROUND, width, terrainHeight, 1, 1);
		matrices.popPose();
		tesselator.end();
	}

	private void renderTexture(PoseStack matrices, BufferBuilder bufferBuilder, ResourceLocation identifier, float width, float height, float u, float v) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.setTexture(this.minecraft, identifier);

		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

		PoseStack.Pose pose = matrices.last();

		bufferBuilder.vertex(pose.pose(), 0, height, 0).uv(0, v).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(pose.pose(), width, height, 0).uv(u, v).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(pose.pose(), width, 0, 0).uv(u, 0).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(pose.pose(), 0, 0, 0).uv(0, 0).color(255, 255, 255, 255).endVertex();
	}

	public void closeScreen(boolean save) {
		if(save) {
			onSave();
		}
		super.onClose();
	}

	/**
	 * Super class should override this and do their own thing when it's suppose to save.
	 */
	public void onSave() {
	}

	protected static Component getBooleanButtonText(boolean state) {
		return Text.translatable(state ? "options.mtr.on" : "options.mtr.off");
	}

	private double easeOutAnimation(double x) {
		return x == 1 ? 1 : 1 - Math.pow(1 - x, 5);
	}
}
