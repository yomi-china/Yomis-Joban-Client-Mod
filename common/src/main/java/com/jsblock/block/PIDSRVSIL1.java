package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Railway Vision PIDS (SIL 1, OCP & SOH)
 * @author LX86, AozoraSky
 * @see PIDSRVBase
 */
public class PIDSRVSIL1 extends PIDSRVBase {

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(0, -2, 0, 16, 9, 16, IBlock.getStatePropertySafe(state, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 9, 8.5, 8.5, 16, 9.5, IBlock.getStatePropertySafe(state, FACING));
        return Shapes.or(shape1, shape2);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityBlockPIDSSIL(pos, state);
    }

    public static class TileEntityBlockPIDSSIL extends TileEntityBlockRVPIDS {

        public static final int MAX_ARRIVALS = 4;

        public TileEntityBlockPIDSSIL(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PIDS_RV_SIL_TILE_ENTITY_1.get(), pos, state);
        }

        @Override
        public int getMaxArrivals() {
            return MAX_ARRIVALS;
        }
    }
}