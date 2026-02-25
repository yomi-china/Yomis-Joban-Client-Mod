package com.jsblock.vermappings.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderHelper {
    public static void setShaderColor(float r, float g, float b, float alpha) {
        RenderSystem.setShaderColor(r, g, b, alpha);
    }

//    public static void fill(PoseStack matrices, int startX, int startY, int width, int height, int color) {
//        net.minecraft.client.gui.Gui.fill(matrices, startX, startY, width, startY + height, color);
//    }

    public static void disableTexture() {
    }

    public static void enableTexture() {
    }

    public static void setTexture(Minecraft minecraft, ResourceLocation identifier) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, identifier);
    }
}