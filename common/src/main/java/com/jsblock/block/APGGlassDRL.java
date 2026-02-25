package com.jsblock.block;

import com.jsblock.Items;
import mtr.block.BlockAPGGlass;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Automatic Platform Gates Glass for DRL APG
 * @author LX86, AozoraSky
 * @since 1.1.6
 */
public class APGGlassDRL extends BlockAPGGlass {
    @Override
    public Item asItem() {
        return Items.APG_GLASS_DRL.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 2 : 16;
        return IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, height, 4.0, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
        return IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, height, 4.0, IBlock.getStatePropertySafe(state, FACING));
    }
}