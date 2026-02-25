package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.Joban;
import com.jsblock.packet.PacketServer;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.UUID;

/**
 * Fare Saver Block
 * @author LX86
 * @since 1.1.4
 * @see ThirdBlockBase
 */
public class FareSaver1 extends ThirdBlockBase implements EntityBlockMapper {
    public FareSaver1(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(3, 0, 6, 13, 16, 9, facing);
    }

    /* If return InteractionResult.FAIL, the hand won't swing */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            UUID playerUUID = player.getUUID();
            BlockEntity entity = world.getBlockEntity(pos);

            if(!(entity instanceof FareSaver1.TileEntityFareSaver)) {
                return InteractionResult.FAIL;
            }

            IBlock.checkHoldingBrush(world, player, () -> {
                /* Have Brush */
                PacketServer.sendFaresaverConfigScreenS2C((ServerPlayer) player, pos, ((TileEntityFareSaver) entity).getDiscount());
            }, () -> {
                /* No Brush */
                int discount = ((FareSaver1.TileEntityFareSaver) entity).getDiscount();
                if(discount > 0) {
                    player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.done", discount), true);
                } else {
                    player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.done_sarcasm", discount), true);
                }
                Joban.discountMap.put(playerUUID, discount);
            });

        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, THIRD);
    }

    @Override
    public BlockEntityType<? extends BlockEntityMapper> getType() {
        return BlockEntityTypes.FARESAVER_1_TILE_ENTITY.get();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new FareSaver1.TileEntityFareSaver(pos, state);
    }

    public static class TileEntityFareSaver extends BlockEntityClientSerializableMapper {
        private int discount = 2;
        private static final String KEY_DISCOUNT = "discount";

        public TileEntityFareSaver(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.FARESAVER_1_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            discount = compoundTag.getInt(KEY_DISCOUNT);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putInt(KEY_DISCOUNT, discount);
        }

        public void setAllData(int discount) {
            EnumThird thisPart = IBlock.getStatePropertySafe(this.getBlockState(), THIRD);
            if(thisPart == EnumThird.LOWER) {
                setData(worldPosition.above(), discount);
                setData(worldPosition.above(2), discount);
            } else if(thisPart == EnumThird.MIDDLE) {
                setData(worldPosition.below(), discount);
                setData(worldPosition.above(), discount);
            } else {
                setData(worldPosition.below(), discount);
                setData(worldPosition.below(2), discount);
            }

            setData(discount);
        }

        public void setData(BlockPos pos, int discount) {
            if(this.level == null) return;

            BlockEntity entity = this.level.getBlockEntity(pos);
            if(entity instanceof TileEntityFareSaver) {
                ((TileEntityFareSaver)entity).setData(discount);
            }
        }

        public void setData(int discount) {
            this.discount = discount;
            this.setChanged();
            this.syncData();
        }

        public int getDiscount() {
            return discount;
        }
    }
}
