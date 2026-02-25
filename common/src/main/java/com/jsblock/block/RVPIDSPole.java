package com.jsblock.block;

import mtr.block.BlockPIDSPole;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Railway Vision PIDS Pole
 * @author LX86
 * @see JobanBlockPoleCheckBase
 */
public class RVPIDSPole extends JobanBlockPoleCheckBase {
    public RVPIDSPole(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return mtr.block.IBlock.getVoxelShapeByDirection(7.5, 0, 8.5, 8.5, 16, 9.5, state.getValue(FACING));
    }

    @Override
    protected boolean isBlock(Block block) {
        return block instanceof BlockPIDSBaseHorizontal || block instanceof BlockPIDSPole || block instanceof RVPIDSPole;
    }

    @Override
    protected Component getTooltipBlockText() {
        return Text.translatable("block.jsblock.rv_pids_pole");
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_SLAB);
    }
}
