package com.jsblock.block;

import com.jsblock.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Circle Wall Block<br>
 * (No plan on supporting this further, use a custom train modelled as a tunnel achieves even better result)
 * @author LX86
 * @since 1.0.0
 */
public class CircleWall extends HorizontalDirectionalBlock {
    public CircleWall() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(8.0f).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return mtr.block.IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 16, state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if(ctx.getClickedFace() == Direction.UP) {
            BlockPos clickedPos = ctx.getClickedPos().below();
            Block selfBlock = this.asBlock();
            BlockState blockBelow = ctx.getLevel().getBlockState(clickedPos);

            if(blockBelow.getBlock() instanceof CircleWall) {
                Block blocc = blockBelow.getBlock();
                Direction blockBelowFacing = IBlock.getStatePropertySafe(blockBelow, FACING);

                if (blocc == Blocks.CIRCLE_WALL_1.get() && selfBlock == Blocks.CIRCLE_WALL_2.get()) {
                    BlockPos placePos = clickedPos.above().relative(blockBelowFacing, 1);
                    if(ctx.getLevel().getBlockState(placePos).canBeReplaced(ctx)) {
                        ctx.getLevel().setBlock(placePos, defaultBlockState().setValue(FACING, blockBelowFacing), 0);
                    }
                    return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                }

                if ((blocc == Blocks.CIRCLE_WALL_4.get() && selfBlock == Blocks.CIRCLE_WALL_5.get()) || blocc == Blocks.CIRCLE_WALL_5.get() && selfBlock == Blocks.CIRCLE_WALL_6.get()) {
                    BlockPos placePos = clickedPos.above().relative(blockBelowFacing.getOpposite(), 1);
                    if(ctx.getLevel().getBlockState(placePos).canBeReplaced(ctx)) {
                        ctx.getLevel().setBlock(placePos, defaultBlockState().setValue(FACING, blockBelowFacing), 0);
                    }
                    return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                }
            }
        }
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
