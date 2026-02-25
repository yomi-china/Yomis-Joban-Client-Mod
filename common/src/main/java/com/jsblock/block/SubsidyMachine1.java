package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.Joban;
import com.jsblock.packet.PacketServer;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Subsidy Machine Block
 * @author LX86, AozoraSky
 * @since 1.0.0
 */
public class SubsidyMachine1 extends HorizontalDirectionalBlock implements EntityBlockMapper {
    private final Map<UUID, Integer> timeouts = new HashMap<>();

    public SubsidyMachine1(Properties settings) {
        super(settings);
    }

    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return mtr.block.IBlock.getVoxelShapeByDirection(3, 0, 0, 13, 17, 3, state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        return mtr.block.IBlock.getVoxelShapeByDirection(3, 0, 0, 13, 16, 3, state.getValue(FACING));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        UUID playerUUID = player.getUUID();
        BlockEntity entity = world.getBlockEntity(pos);
        mtr.data.TicketSystem.addObjectivesIfMissing(world);

        IBlock.checkHoldingBrush(world, player, () -> {
            /* Have Brush */
            if(entity instanceof SubsidyMachine1.TileEntitySubsidyMachine) {
                // Clears Timeout as they might want to reconfigure the timeout
                timeouts.remove(playerUUID);
                PacketServer.sendSubsidyConfigScreenS2C((ServerPlayer) player, pos, ((SubsidyMachine1.TileEntitySubsidyMachine)entity).getPricePerClick(), ((SubsidyMachine1.TileEntitySubsidyMachine)entity).getTimeout());
            }
        }, () -> {
            /* No Brush */
            if(entity instanceof SubsidyMachine1.TileEntitySubsidyMachine) {
                int subsidyPrice = ((SubsidyMachine1.TileEntitySubsidyMachine) entity).getPricePerClick();
                int timeoutTick = ((SubsidyMachine1.TileEntitySubsidyMachine) entity).getTimeout() * 20;

                if(timeouts.containsKey(playerUUID)) {
                    int timeoutLeft = timeouts.get(playerUUID) - Joban.getGameTick();
                    if(timeoutLeft <= 0) {
                        timeouts.remove(playerUUID);
                    } else {
                        player.displayClientMessage(Text.translatable("gui.jsblock.subsidy_timeout", timeoutLeft / 20), true);
                        return;
                    }
                }

                Score balanceScore = mtr.data.TicketSystem.getPlayerScore(world, player, mtr.data.TicketSystem.BALANCE_OBJECTIVE);
                long finalPrice = (long)balanceScore.getScore() + subsidyPrice;

                if(finalPrice <= Integer.MAX_VALUE) {
                    balanceScore.setScore(balanceScore.getScore() + subsidyPrice);
                    player.displayClientMessage(Text.translatable("gui.jsblock.subsidy", subsidyPrice, balanceScore.getScore()), true);
                    timeouts.put(playerUUID, Joban.getGameTick() + timeoutTick);
                } else {
                    // Don't let it overflow to negative
                    player.displayClientMessage(Text.translatable("gui.jsblock.subsidy_maxed"), true);
                }
            }
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntityType<? extends BlockEntityMapper> getType() {
        return BlockEntityTypes.SUBSIDY_MACHINE_TILE_ENTITY_1.get();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new SubsidyMachine1.TileEntitySubsidyMachine(pos, state);
    }

    public static class TileEntitySubsidyMachine extends BlockEntityMapper {
        private int pricePerClick = 10;
        private int timeout = 0;
        private static final String KEY_PRICE_PER_CLICK = "price_per_click";
        private static final String KEY_TIMEOUT = "timeout";

        public TileEntitySubsidyMachine(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SUBSIDY_MACHINE_TILE_ENTITY_1.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            pricePerClick = compoundTag.getInt(KEY_PRICE_PER_CLICK);
            timeout = compoundTag.getInt(KEY_TIMEOUT);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putInt(KEY_PRICE_PER_CLICK, pricePerClick);
            compoundTag.putInt(KEY_TIMEOUT, timeout);
        }

        public void setData(int pricePerClick, int timeout) {
            this.pricePerClick = pricePerClick;
            this.timeout = timeout;
            this.setChanged();
        }

        public int getPricePerClick() {
            return pricePerClick;
        }

        public int getTimeout() {
            return timeout;
        }
    }
}