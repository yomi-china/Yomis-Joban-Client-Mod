package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockSignalLightBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Static Signal Light Block (Green)
 * @author LX86
 * @since 1.0.0
 * @see BlockSignalLightBase
 */
public class SignalLightGreen extends BlockSignalLightBase {

    public SignalLightGreen(Properties settings) {
        super(settings, 2, 14);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntitySignalLightGreen(pos, state);
    }

    public static class TileEntitySignalLightGreen extends BlockEntityMapper {

        public TileEntitySignalLightGreen(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SIGNAL_LIGHT_GREEN_ENTITY.get(), pos, state);
        }
    }
}