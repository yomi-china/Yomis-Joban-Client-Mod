package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * KCR Emergency Stop Sign (Pointing to the EMG button)
 * @author LX86
 * @since 1.1.0
 */
public class KCREmgStopSign extends HorizontalDirectionalBlock {

    public static final BooleanProperty RIGHT = BooleanProperty.create("right");

    public KCREmgStopSign(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        /* The following code will only be ran if player is holding the MTR brush item */
        return IBlock.checkHoldingBrush(world, player, () -> {
            /* Cycle the boolean value of RIGHT */
            world.setBlockAndUpdate(pos, state.cycle(RIGHT));
        });
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return mtr.block.IBlock.getVoxelShapeByDirection(0, 0, 7.5, 26, 7, 10.5, state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getClockWise()).setValue(RIGHT, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, RIGHT);
    }
}
