package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Water Machine block
 * @author LX86
 * @since 1.0.0
 * @see mtr.block.BlockDirectionalDoubleBlockBase
 */
public class WaterMachine1 extends mtr.block.BlockDirectionalDoubleBlockBase {
    public WaterMachine1(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(2.5, 0, 0, 13.5, 16, 11, facing);
    }

    /* On player use (Right-clicked without shift)
    need to return an InteractionResult, if it's InteractionResult.FAIL, the hand won't swing */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(player.isHolding(Items.GLASS_BOTTLE)) {
                ItemStack bottleItem = player.getItemInHand(hand);
                ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                /* Remove 1 water bottle from player */
                bottleItem.shrink(1);
                /* If player is not holding anything after removing 1 water bottle, give the water bottle directly to their hand */
                if (bottleItem.isEmpty()) {
                    player.setItemInHand(hand, waterBottle);
                } else {
                /* Otherwise, just give them the water bottle (And drop the bottle if can't give, inventory is probably full?) */
                    if (!player.addItem(waterBottle)) {
                        player.drop(waterBottle, false);
                    }
                    //player.addItem(waterBottle);
                }

                world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}
