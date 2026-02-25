package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.Items;
import mtr.block.BlockAPGDoor;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Automatic Platform Gates Door for DRL APG
 * @author LX86, AozoraSky
 * @since 1.1.6
 */
public class APGDoorDRL extends BlockAPGDoor {
    @Override
    public Item asItem() {
        return Items.APG_DOOR_DRL.get();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDRLAPGDoor(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 2 : 16;
        return IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, height, 4.0, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        final int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
		final BlockEntity entity = world.getBlockEntity(pos);
        return entity instanceof TileEntityPSDAPGDoorBase && ((TileEntityPSDAPGDoorBase) entity).isOpen() ? Shapes.empty() : IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, height, 4.0, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        /* Do not render with traditinal block model, as we render them with Block Entity Renderer */
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public static class TileEntityDRLAPGDoor extends TileEntityPSDAPGDoorBase {

        public TileEntityDRLAPGDoor(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.DRL_APG_DOOR_TILE_ENTITY.get(), pos, state);
        }
    }
}