package com.jsblock.block;

import com.jsblock.packet.PacketServer;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Set;

/**
 * Abstract class for Railway Vision type PIDS
 * @author LX86
 * @since 1.1.0
 */
public abstract class PIDSRVBase extends JobanPIDSBase {

    /* If return InteractionResult.FAIL, the hand won't swing */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            BlockPos otherPos = pos.relative(IBlock.getStatePropertySafe(state, FACING));
            BlockEntity entity1 = world.getBlockEntity(pos);
            BlockEntity entity2 = world.getBlockEntity(otherPos);
            if (entity1 instanceof TileEntityBlockRVPIDS && entity2 instanceof TileEntityBlockRVPIDS) {
                ((TileEntityBlockRVPIDS) entity1).syncData();
                ((TileEntityBlockRVPIDS) entity2).syncData();
                PacketServer.sendRVPIDSConfigScreenS2C((ServerPlayer) player, pos, otherPos, ((TileEntityBlockRVPIDS) entity1).getMaxArrivals(), ((TileEntityBlockRVPIDS) entity1).getHidePlatformNumber(), ((TileEntityBlockRVPIDS) entity1).getPresetID());
            }
        });
    }

    public abstract static class TileEntityBlockRVPIDS extends JobanPIDSBase.TileEntityBlockJobanPIDS {
        private boolean hidePlatformNumber;
        private static final String KEY_HIDE_PLATFORM_NUMBER = "hide_platform_number";

        public TileEntityBlockRVPIDS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            this.hidePlatformNumber = compoundTag.getBoolean(KEY_HIDE_PLATFORM_NUMBER);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putBoolean(KEY_HIDE_PLATFORM_NUMBER, this.hidePlatformNumber);
            super.writeCompoundTag(compoundTag);
        }

        public void setData(String[] messages, boolean[] hideArrival, Set<Long> platformIds, int displayPage, boolean hidePlatformNumber, String presetID) {
            super.setData(messages, hideArrival, platformIds, displayPage, presetID);
            this.hidePlatformNumber = hidePlatformNumber;
            this.setChanged();
            this.syncData();
        }

        @Override
        public abstract int getMaxArrivals();

        public boolean getHidePlatformNumber() {
            return hidePlatformNumber;
        }
    }
}