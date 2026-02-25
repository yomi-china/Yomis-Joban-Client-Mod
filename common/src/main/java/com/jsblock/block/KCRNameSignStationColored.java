package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * KCR Station Name Sign (Station Colored)
 * @author LX86
 * @since 1.1.4
 * @see com.jsblock.block.KCRNameSign
 */
public class KCRNameSignStationColored extends KCRNameSign {

    public KCRNameSignStationColored(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntityType<? extends BlockEntityMapper> getType() {
        return BlockEntityTypes.KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY.get();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityKCRNameStationColorSign(pos, state);
    }

    public static class TileEntityKCRNameStationColorSign extends FontBase.TileEntityBlockFontBase {

        public TileEntityKCRNameStationColorSign(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public String getDefaultFont() {
            return FONT_NAME;
        }
    }
}