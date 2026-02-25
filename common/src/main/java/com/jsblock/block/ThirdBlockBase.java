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

/**
 * Abstract class that designed for 3 block high blocks.
 * @author LX86
 * @since 1.1.5
 */
public abstract class ThirdBlockBase extends HorizontalDirectionalBlock implements IBlock {
    public ThirdBlockBase(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if ((direction == Direction.UP && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.UPPER || direction == Direction.DOWN && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.LOWER) && !newState.is(this)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        switch (IBlock.getStatePropertySafe(state, THIRD)) {
            case MIDDLE:
                IBlock.onBreakCreative(world, player, pos.below());
                break;
            case UPPER:
                IBlock.onBreakCreative(world, player, pos.below(2));
                break;
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return IBlock.isReplaceable(ctx, Direction.UP, 3) ? defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()) : null;
    }

    /* On block place */
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide) {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            world.setBlock(pos.above(), defaultBlockState().setValue(FACING, facing).setValue(THIRD, EnumThird.MIDDLE), 3);
            world.setBlock(pos.above(2), defaultBlockState().setValue(FACING, facing).setValue(THIRD, EnumThird.UPPER), 3);
            world.updateNeighborsAt(pos, Blocks.AIR);
            state.updateNeighbourShapes(world, pos, 3);
        }
    }
}
