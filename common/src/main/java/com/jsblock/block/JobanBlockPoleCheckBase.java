package com.jsblock.block;

import mtr.block.BlockPoleCheckBase;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

/**
 * Abstract class that supports detecting whether a slab is above the block, and change its model accordingly.<br>
 * Wanna merge it to the main mod, but overflowing the block model over 16 pixels is kinda wonky.<br>
 * Extended from MTR's pole base
 * @author LX86
 * @since 1.1.4
 * @see mtr.block.BlockPoleCheckBase
 */
public abstract class JobanBlockPoleCheckBase extends BlockPoleCheckBase {
    public static final BooleanProperty IS_SLAB = BooleanProperty.create("is_slab");

    public JobanBlockPoleCheckBase(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(IS_SLAB, false));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        BlockState blockAbove = world.getBlockState(pos.above());
        if(!isUpperSlab(blockAbove)) {
            return state.setValue(IS_SLAB, false);
        } else {
            return state.setValue(IS_SLAB, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState stateBelow = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
        BlockState stateAbove = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
        if(!this.isBlock(stateBelow.getBlock())) {
            return null;
        }

        if (stateAbove.getBlock() instanceof SlabBlock) {
            if (stateAbove.getValue(SlabBlock.TYPE) == SlabType.TOP) {
                return this.placeWithState(stateBelow).setValue(IS_SLAB, true);
            }
        }
        return this.placeWithState(stateBelow);
    }

    private static boolean isUpperSlab(BlockState state) {
        if(!(state.getBlock() instanceof SlabBlock) || (state.is(Blocks.AIR))) {
            return false;
        }

        return IBlock.getStatePropertySafe(state, SlabBlock.TYPE) == SlabType.TOP;
    }
}
