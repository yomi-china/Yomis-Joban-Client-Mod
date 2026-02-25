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
 * Abstract class for Joban PIDS, used for storing presets ID
 * @author LX86
 * @see com.jsblock.block.BlockPIDSBaseHorizontal
 */
public abstract class JobanPIDSBase extends BlockPIDSBaseHorizontal {
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            BlockPos otherPos = pos.relative(IBlock.getStatePropertySafe(state, FACING));
            BlockEntity entity1 = world.getBlockEntity(pos);
            BlockEntity entity2 = world.getBlockEntity(otherPos);
            if (entity1 instanceof TileEntityBlockJobanPIDS && entity2 instanceof TileEntityBlockJobanPIDS) {
                ((TileEntityBlockJobanPIDS) entity1).syncData();
                ((TileEntityBlockJobanPIDS) entity2).syncData();
                PacketServer.sendJobanPIDSConfigScreenS2C((ServerPlayer) player, pos, otherPos, ((TileEntityBlockJobanPIDS)entity1).getMaxArrivals(), ((TileEntityBlockJobanPIDS) entity1).getPresetID());
            }
        });
    }

    public abstract static class TileEntityBlockJobanPIDS extends TileEntityBlockPIDSBaseHorizontal {
        private String presetID = "";
        private static final String KEY_SCREEN_ID = "preset_id";

        public TileEntityBlockJobanPIDS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            this.presetID = compoundTag.getString(KEY_SCREEN_ID);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putString(KEY_SCREEN_ID, presetID);
            super.writeCompoundTag(compoundTag);
        }

        @Override
        public abstract int getMaxArrivals();

        public void setData(String[] messages, boolean[] hideArrival, Set<Long> platformIds, int displayPage, String screenID) {
            super.setData(messages, hideArrival, platformIds, displayPage);
            this.presetID = screenID;
            this.setChanged();
            this.syncData();
        }

        public String getPresetID() {
            return presetID;
        }
    }
}