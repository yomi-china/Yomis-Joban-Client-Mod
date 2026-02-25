package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockSignalLightBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Static Signal Light Block (Blue)
 * @author LX86
 * @since 1.0.0
 * @see BlockSignalLightBase
 */
public class SignalLightBlue extends BlockSignalLightBase {

    public SignalLightBlue(Properties settings) {
        super(settings, 2, 14);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntitySignalLightBlue(pos, state);
    }

    public static class TileEntitySignalLightBlue extends BlockEntityMapper {

        public TileEntitySignalLightBlue(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SIGNAL_LIGHT_BLUE_ENTITY.get(), pos, state);
        }
    }
}