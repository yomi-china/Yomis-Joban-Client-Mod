package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Abstract class for a multiblock structure (1x2), extend this from your own class.
 * @author LX86
 * @since 1.0.0
 */
public abstract class HorizontalMultiBlockBase extends HorizontalDirectionalBlock {
    public static final BooleanProperty LEFT = BooleanProperty.create("left");

    public HorizontalMultiBlockBase(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        /* This gets the direction of the block is facing, then rotate 90 degree *Counter* Clockwise if this is the left part. Otherwise, rotate 90 degree Clockwise if this is the right part */
        /* The result is that if it's the left part, it will check for right. If it's right part, check for the left. */
        final Direction facing = IBlock.getStatePropertySafe(state, LEFT) ? IBlock.getStatePropertySafe(state, FACING).getCounterClockWise() : IBlock.getStatePropertySafe(state, FACING).getClockWise();

        /* If the direction of the block update is issued in the same direction as the "facing", and the block is not ours: Destroy the block */
        /* (facing == direction should only be true if another part of this block is placed) */
        if (facing == direction && !newState.is(this)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final Direction direction = ctx.getHorizontalDirection().getOpposite();
        return IBlock.isReplaceable(ctx, direction.getCounterClockWise(), 2) ? defaultBlockState().setValue(FACING, direction).setValue(LEFT, true) : null;
    }

    /* On block place */
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!world.isClientSide) {
            final Direction direction = IBlock.getStatePropertySafe(state, FACING);
            world.setBlock(pos.relative(direction.getCounterClockWise()), defaultBlockState().setValue(FACING, direction).setValue(LEFT, false), 3);
            world.updateNeighborsAt(pos, Blocks.AIR);
            state.updateNeighbourShapes(world, pos, 3);
        }
    }

    /* On block break */
    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!IBlock.getStatePropertySafe(state, LEFT)) {
            IBlock.onBreakCreative(world, player, pos.relative(IBlock.getStatePropertySafe(state, FACING).getClockWise()));
        }
        super.playerWillDestroy(world, pos, state, player);
    }
}