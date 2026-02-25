package com.jsblock.block;

import com.jsblock.Particles;
import com.jsblock.vermappings.block.ParticleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Invisible Light Block
 * @author LX86
 * @since 1.0.0
 */
public class LightBlock extends ParticleBase {

    public static final int MAX_LEVEL = 15;

    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("level", 0, MAX_LEVEL);

    public LightBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(LIGHT_LEVEL, MAX_LEVEL));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return Shapes.box(0f, 0f, 0f, 1f, 1f, 1f);
    }

    /* Return the Voxel Shape (The COLLISION hitbox of the block) */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos) {
        if (Minecraft.getInstance().player.isHolding(this.asItem())) {
            world.addParticle(Particles.LIGHT_BLOCK.get(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }

    /* On player use (Right-clicked without shift)
    need to return an InteractionResult, if it's InteractionResult.FAIL, the hand won't swing */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        /* If player is holding this (Light Block) as an item */
        if (player.isHolding(this.asItem())) {
            /* Cycle the light level */
            world.setBlockAndUpdate(pos, state.cycle(LIGHT_LEVEL));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    /* Must return true if you want light to pass through the block */
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }
}