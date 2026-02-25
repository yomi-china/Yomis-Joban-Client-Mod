package com.jsblock.block;

import mtr.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Thales Ticket Barrier (Metal Only)
 * @author LX86, AozoraSky
 */
public class TicketBarrier1Decor extends HorizontalDirectionalBlock {
    public static final IntegerProperty FENCE_TYPE = IntegerProperty.create("type", 0, 10);
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

    public TicketBarrier1Decor() {
        super(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(FENCE_TYPE, 0).setValue(FLIPPED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState stateNear = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getHorizontalDirection().getCounterClockWise()));
        return getFenceState(stateNear, null, defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()), ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        return getFenceState(newState, direction, state, world, pos);
    }

    private BlockState getFenceState(BlockState stateNear, Direction direction, BlockState state, LevelAccessor world, BlockPos pos) {
        if(stateNear.is(com.jsblock.Blocks.TICKET_BARRIER_1_EXIT.get()) || stateNear.is(com.jsblock.Blocks.TICKET_BARRIER_1_ENTRANCE.get()) || stateNear.is(this)) {
            return state;
        }

        Direction thisBlockDirection = IBlock.getStatePropertySafe(state, FACING);
        boolean hasBlockNextToFence = world.getBlockState(pos.relative(thisBlockDirection.getCounterClockWise())).getBlock() != net.minecraft.world.level.block.Blocks.AIR;

        if(stateNear.is(Blocks.GLASS_FENCE_CIO.get()) || stateNear.is(Blocks.GLASS_FENCE_CKT.get()) || stateNear.is(Blocks.GLASS_FENCE_HEO.get()) || stateNear.is(Blocks.GLASS_FENCE_MOS.get()) || stateNear.is(Blocks.GLASS_FENCE_PLAIN.get()) || stateNear.is(Blocks.GLASS_FENCE_SHM.get()) || stateNear.is(Blocks.GLASS_FENCE_STAINED.get()) || stateNear.is(Blocks.GLASS_FENCE_STW.get()) || stateNear.is(Blocks.GLASS_FENCE_TSH.get()) || stateNear.is(Blocks.GLASS_FENCE_WKS.get())) {

            boolean valid = (IBlock.getStatePropertySafe(stateNear, FACING) == thisBlockDirection) || (IBlock.getStatePropertySafe(stateNear, FACING) == thisBlockDirection.getOpposite());
            boolean flipped = !(IBlock.getStatePropertySafe(stateNear, FACING) == IBlock.getStatePropertySafe(state, FACING));

            /* Only accept direction from left or right */
            if(direction != thisBlockDirection.getClockWise() && direction != thisBlockDirection.getCounterClockWise()) {
                valid = false;
            }

            if(valid) {
                if(stateNear.is(Blocks.GLASS_FENCE_CIO.get())) {
                    return state.setValue(FENCE_TYPE, 1).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_CKT.get())) {
                    return state.setValue(FENCE_TYPE, 2).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_HEO.get())) {
                    return state.setValue(FENCE_TYPE, 3).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_MOS.get())) {
                    return state.setValue(FENCE_TYPE, 4).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_PLAIN.get())) {
                    return state.setValue(FENCE_TYPE, 5).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_SHM.get())) {
                    return state.setValue(FENCE_TYPE, 6).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_STAINED.get())) {
                    return state.setValue(FENCE_TYPE, 7).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_STW.get())) {
                    return state.setValue(FENCE_TYPE, 8).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_TSH.get())) {
                    return state.setValue(FENCE_TYPE, 9).setValue(FLIPPED, flipped);
                }

                if(stateNear.is(Blocks.GLASS_FENCE_WKS.get())) {
                    return state.setValue(FENCE_TYPE, 10).setValue(FLIPPED, flipped);
                }
            }
        }
        if(hasBlockNextToFence) {
            return state;
        }
        return state.setValue(FENCE_TYPE, 0);
    }

    /* Return the Voxel Shape (The Visual hitbox of the block) */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final int type = IBlock.getStatePropertySafe(state, FENCE_TYPE);
        final boolean flipped = IBlock.getStatePropertySafe(state, FLIPPED);
        final VoxelShape barrierShape = IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 16, 16, facing);

        if(type > 0) {
            VoxelShape fenceShape = flipped ? IBlock.getVoxelShapeByDirection(0, 0, 13, 12, 19, 16, facing) : IBlock.getVoxelShapeByDirection(0, 0, 0, 12, 19, 3, facing);
            return Shapes.or(fenceShape, barrierShape);
        }

        return barrierShape;
    }

    /* Return the Voxel Shape (The Collision hitbox of the block) */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final int type = IBlock.getStatePropertySafe(state, FENCE_TYPE);
        final boolean flipped = IBlock.getStatePropertySafe(state, FLIPPED);
        final VoxelShape barrierShape = IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 24, 16, facing);
        final VoxelShape fenceShape = flipped ? IBlock.getVoxelShapeByDirection(0, 0, 13, 12, 24, 16, facing) : IBlock.getVoxelShapeByDirection(0, 0, 0, 12, 24, 3, facing);

        return type > 0 ? Shapes.or(barrierShape, fenceShape) : barrierShape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FENCE_TYPE, FLIPPED);
    }
}
