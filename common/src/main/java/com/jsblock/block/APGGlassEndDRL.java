package com.jsblock.block;

import com.jsblock.Items;
import mtr.block.BlockAPGGlassEnd;
import mtr.block.BlockPSDAPGGlassEndBase;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Automatic Platform Gates Glass End for DRL APG
 * @author LX86, AozoraSky
 * @since 1.1.6
 */
public class APGGlassEndDRL extends BlockAPGGlassEnd {
    @Override
    public Item asItem() {
        return Items.APG_GLASS_END_DRL.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        VoxelShape superShape = super.getShape(state, world, pos, collisionContext);
        int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 2 : 16;
        boolean leftAir = IBlock.getStatePropertySafe(state, TOUCHING_LEFT) == BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.AIR;
        boolean rightAir = IBlock.getStatePropertySafe(state, TOUCHING_RIGHT) == BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.AIR;
        return getEndOutlineShape(superShape, state, height, 4, leftAir, rightAir);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        return super.getShape(state, world, pos, collisionContext);
    }
}