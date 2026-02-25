package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockSignalLightBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Static Signal Light Block (Red)
 * @author LX86
 * @since 1.0.0
 * @see BlockSignalLightBase
 */
public class SignalLightRed1 extends BlockSignalLightBase {

    public SignalLightRed1(Properties settings) {
        super(settings, 2, 14);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntitySignalLightRed(pos, state);
    }

    public static class TileEntitySignalLightRed extends BlockEntityMapper {

        public TileEntitySignalLightRed(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SIGNAL_LIGHT_RED_ENTITY_1.get(), pos, state);
        }
    }
}
