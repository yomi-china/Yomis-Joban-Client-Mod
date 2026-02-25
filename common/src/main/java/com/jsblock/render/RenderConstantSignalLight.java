package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.IDrawing;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;

/**
 * Render a persistent signal light.<br>
 * Called from {@link RenderConstantSignalBase}
 * @author LX86
 * @see com.jsblock.block.SignalLightRed1
 * @see com.jsblock.block.SignalLightRed2
 * @see com.jsblock.block.SignalLightBlue
 * @see com.jsblock.block.SignalLightGreen
 */
public class RenderConstantSignalLight<T extends BlockEntityMapper> extends RenderConstantSignalBase<T> {

    final int constantColor;
    final boolean renderOnTop;

    /**
     * Constructor for rendering a persistent signal light.
     * @param dispatcher
     * @param isSingleSided
     * @param constantColor The color to be displayed
     * @param renderOnTop Whether red should be on top
     */
    public RenderConstantSignalLight(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided, int constantColor, boolean renderOnTop) {
        super(dispatcher, isSingleSided);
        this.constantColor = constantColor;
        this.renderOnTop = renderOnTop;
    }

    @Override
    protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float delta, Direction facing, boolean isOccupied, boolean isBackSide) {
        /* The super class has already checked the config that rendering is enabled */
        float y = renderOnTop ? 0.4375F : 0.0625F;
        IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), constantColor, MAX_LIGHT_GLOWING);
    }
}
