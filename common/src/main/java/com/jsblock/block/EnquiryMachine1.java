package com.jsblock.block;

import mtr.SoundEvents;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * MTR URL Enquiry Machine
 * @author LX86
 * @since 1.0.0
 */
public class EnquiryMachine1 extends mtr.block.BlockDirectionalDoubleBlockBase {
    public EnquiryMachine1(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
            VoxelShape vx1 = IBlock.getVoxelShapeByDirection(4, 0, 7, 12, 5.62, 14, facing);
            VoxelShape vx2 = IBlock.getVoxelShapeByDirection(4, 5.62, 8.275, 12, 10.12, 8.425, facing);
            // Merge vx1 and vx2 together
            return Shapes.or(vx1, vx2);
        } else {
            /* Otherwise, return the lower block shape */
            return IBlock.getVoxelShapeByDirection(4, 0, 7, 12, 16, 14, facing);
        }
    }

    /* If return InteractionResult.FAIL, the hand won't swing */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            final int playerScore = TicketSystem.getPlayerScore(world, player, TicketSystem.BALANCE_OBJECTIVE).getScore();
            /* Send action bar to the player displaying playerScore */
            player.displayClientMessage(Text.translatable("gui.mtr.balance", String.valueOf(playerScore)), true);
            /* Play a sound effect (Sound from the MTR Mod) */
            world.playSound(null, pos, SoundEvents.TICKET_PROCESSOR_ENTRY, SoundSource.BLOCKS, 1, 1);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}
