package com.jsblock.block;

import mtr.block.IBlock;
import mtr.data.Train;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Authorize Button Block (Operator Button)
 * @author LX86
 * @since 1.0.4
 */
public class AuthorizeButton extends BlockDirectionalMapper {
    private static final BooleanProperty LIT = BooleanProperty.create("lit");
    private static final int TIMEOUT = 80;

    public AuthorizeButton(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        return mtr.block.IBlock.getVoxelShapeByDirection(5, 4.75, 0, 11, 11.25, 0.2, state.getValue(FACING));
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldBlockState, boolean isMoving) {
        updateNearby(world, pos);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldBlockState, boolean isMoving) {
        updateNearby(world, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos) {
        if(world == null || world.isClientSide()) return;
        world.setBlockAndUpdate(pos, state.setValue(LIT, false));
        updateNearby(world, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && Train.isHoldingKey(player)) {
            world.setBlockAndUpdate(pos, state.setValue(LIT, true));
            updateNearby(world, pos);
            Utilities.scheduleBlockTick(world, pos, this, TIMEOUT);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(LIT, false);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return IBlock.getStatePropertySafe(state, LIT) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return IBlock.getStatePropertySafe(state, LIT) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    protected void updateNearby(Level world, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            world.updateNeighborsAt(pos.relative(direction), this);
        }
    }
}
