package com.jsblock.block;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Abstract class that supports storing a font id.<br>
 * @author LX86
 * @since 1.1.4
 */
public abstract class FontBase extends HorizontalDirectionalBlock implements EntityBlockMapper {

    public FontBase(Properties properties) {
        super(properties);
    }

    public abstract static class TileEntityBlockFontBase extends BlockEntityClientSerializableMapper {
        private String font = null;
        private static final String KEY_FONT = "font";

        public TileEntityBlockFontBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            this.font = compoundTag.getString(KEY_FONT);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putString(KEY_FONT, this.font == null || this.font.isEmpty() ? getDefaultFont() : this.font);
            super.writeCompoundTag(compoundTag);
        }

        public void setData(String[] messages, boolean[] hideArrival, String chinFont) {
            this.font = chinFont;
            this.setChanged();
            this.syncData();
        }

        public String getFont() {
            return font == null || font.isEmpty() ? getDefaultFont() : font;
        }

        public abstract String getDefaultFont();
    }
}