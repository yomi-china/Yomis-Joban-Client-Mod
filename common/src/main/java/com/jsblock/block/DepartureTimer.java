package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Departure Timer Block
 * @author LX86
 * @see com.jsblock.block.FontBase
 */
public class DepartureTimer extends FontBase {
    private static final String FONT_NAME = "jsblock:deptimer";

    public DepartureTimer(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(2.7, 0, 0, 13.3, 10.7, 13, facing);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDepartureTimer(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static class TileEntityDepartureTimer extends TileEntityBlockFontBase {

        public TileEntityDepartureTimer(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.DEPARTURE_TIMER_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public String getDefaultFont() {
            return FONT_NAME;
        }
    }
}