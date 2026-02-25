package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * South Island Line Emergency Stop Button (Named helpline6 internally)
 * @author AozoraSky
 * @since 1.1.1
 */
public class EmgStop6 extends ThirdBlockBase {
    public EmgStop6(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (IBlock.getStatePropertySafe(state, THIRD) == EnumThird.UPPER) {
            return IBlock.getVoxelShapeByDirection(4, 0, 6, 12, 12, 7, facing);
        } else {
            return IBlock.getVoxelShapeByDirection(4, 0, 6, 12, 16, 7, facing);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, THIRD);
    }
}
