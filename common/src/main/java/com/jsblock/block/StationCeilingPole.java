package com.jsblock.block;

import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.jsblock.block.HorizontalMultiBlockBase.LEFT;

/**
 * Station Ceiling Pole Block
 * @author AozoraSky
 * @since 1.1.2
 * @see JobanBlockPoleCheckBase
 */
public class StationCeilingPole extends JobanBlockPoleCheckBase {
    public StationCeilingPole(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (IBlock.getStatePropertySafe(state, LEFT)) {
            return IBlock.getVoxelShapeByDirection(10.5, 0, 7.5, 11.5, 16, 8.5, IBlock.getStatePropertySafe(state, FACING));
        } else {
            return IBlock.getVoxelShapeByDirection(5.5, 0, 7.5, 6.5, 16, 8.5, IBlock.getStatePropertySafe(state, FACING));
        }
    }

    @Override
    protected boolean isBlock(Block block) {
        return block instanceof StationCeiling1 || block instanceof StationCeilingPole;
    }

    protected BlockState placeWithState(BlockState stateBelow) {
        return this.defaultBlockState().setValue(FACING, IBlock.getStatePropertySafe(stateBelow, FACING)).setValue(LEFT, IBlock.getStatePropertySafe(stateBelow, LEFT));
    }

    @Override
    protected Component getTooltipBlockText() {
        return Text.translatable("block.jsblock.station_ceiling_pole");
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEFT, IS_SLAB);
    }
}
