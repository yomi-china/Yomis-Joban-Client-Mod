package com.jsblock.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Spot Light
 * @author LX86
 * @since 1.0.0
 */
public class Light2 extends Block {
    public static final BooleanProperty CEILING = BooleanProperty.create("ceiling");

    public Light2(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(CEILING, true));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(CEILING) ? Block.box(4, 15.9, 4, 12, 16, 12) : Block.box(4, 0, 4, 12, 0.1, 12);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CEILING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockAbove = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
        BlockState blockBelow = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
        if(ctx.getClickedFace() == Direction.UP) {
            return blockBelow.getBlock().equals(Blocks.AIR) ? null : defaultBlockState().setValue(CEILING, false);
        } else {
            if (blockAbove.getBlock().equals(Blocks.AIR)) {
                return blockBelow.getBlock().equals(Blocks.AIR) ? null : defaultBlockState().setValue(CEILING, false);
            } else {
                return defaultBlockState().setValue(CEILING, true);
            }
        }
    }
}
