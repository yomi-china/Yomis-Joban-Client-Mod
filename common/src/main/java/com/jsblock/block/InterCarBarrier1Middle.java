package com.jsblock.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LRT Car Barrier (Middle)
 * @author AozoraSky
 * @since 1.1.5
 */
public class InterCarBarrier1Middle extends HorizontalDirectionalBlock {
    public InterCarBarrier1Middle(Properties settings) {
        super(settings);
    }

    /* Return the Voxel Shape (The VISUAL hitbox of the block) */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return mtr.block.IBlock.getVoxelShapeByDirection(0, 0, 4, 16, 16, 14, state.getValue(FACING));
    }

    /* Return the Voxel Shape (The COLLISION hitbox of the block) */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return mtr.block.IBlock.getVoxelShapeByDirection(0, 0, 4, 16, 24, 14, state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
