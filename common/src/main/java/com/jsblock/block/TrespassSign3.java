package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LRT Trespass Signage
 * @author AozoraSky
 * @since 1.1.5
 * @see mtr.block.BlockDirectionalDoubleBlockBase
 */
public class TrespassSign3 extends mtr.block.BlockDirectionalDoubleBlockBase {

    public TrespassSign3(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        /* If the block is the upper one */
        if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
            VoxelShape sign = IBlock.getVoxelShapeByDirection(5.5, 2, 7.97, 10.5, 10, 8.03, facing);
            VoxelShape pole = IBlock.getVoxelShapeByDirection(7.5, 0, 7, 8.5, 11, 8, facing);
            return Shapes.or(sign, pole);
        } else {
            return IBlock.getVoxelShapeByDirection(7.5, 0, 7, 8.5, 16, 8, facing);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}