package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * KCR Station Name Sign
 * @author LX86
 * @since 1.0.4
 * @see com.jsblock.block.FontBase
 */
public class KCRNameSign extends FontBase {

    public static final BooleanProperty EXIT_ON_LEFT = BooleanProperty.create("exit_on_left");
    protected static final String FONT_NAME = "jsblock:kcr_sign";

    public KCRNameSign(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(-7, 2, 5.5, 23, 16, 10.51, facing);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        /* The following code will only be ran if player is holding the MTR brush item */
        return IBlock.checkHoldingBrush(world, player, () -> {
            /* Cycle the boolean value of EXIT_ON_LEFT */
            world.setBlockAndUpdate(pos, state.cycle(EXIT_ON_LEFT));
        });
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(EXIT_ON_LEFT, false);
    }

    @Override
    public BlockEntityType<? extends BlockEntityMapper> getType() {
        return BlockEntityTypes.KCR_NAME_SIGN_TILE_ENTITY.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXIT_ON_LEFT);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityKCRNameSign(pos, state);
    }

    public static class TileEntityKCRNameSign extends FontBase.TileEntityBlockFontBase {

        public TileEntityKCRNameSign(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.KCR_NAME_SIGN_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public String getDefaultFont() {
            return FONT_NAME;
        }
    }
}