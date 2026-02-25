package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Station Ceiling Panel Block
 * @author AozoraSky
 * @since 1.1.2
 */
public class StationCeiling1 extends HorizontalMultiBlockBase {

    public StationCeiling1(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        VoxelShape shape1;
        VoxelShape shape2;
        if (IBlock.getStatePropertySafe(state, LEFT)) {
            shape1 = IBlock.getVoxelShapeByDirection(0, 8, 1, 15.5, 9, 15, IBlock.getStatePropertySafe(state, FACING));
            shape2 = IBlock.getVoxelShapeByDirection(10.5, 9, 7.5, 11.5, 16, 8.5, IBlock.getStatePropertySafe(state, FACING));
        } else {
            shape1 = IBlock.getVoxelShapeByDirection(0.5, 8, 1, 16, 9, 15, IBlock.getStatePropertySafe(state, FACING));
            shape2 = IBlock.getVoxelShapeByDirection(5.5, 9, 7.5, 6.5, 16, 8.5, IBlock.getStatePropertySafe(state, FACING));
        }
        return Shapes.or(shape1, shape2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEFT);
    }
}