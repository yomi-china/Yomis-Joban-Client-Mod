package com.jsblock.block;

import com.jsblock.vermappings.block.DoorBase;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Iron Door that will automatically open when you get close
 * @author LX86
 * @since 1.0.4
 */
public class AutoDoor extends DoorBase {

    public AutoDoor(Properties settings) {
        super(settings);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        Utilities.scheduleBlockTick(world, pos, state.getBlock(), 0);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos) {
        if (world == null || world.isClientSide) {
            return;
        }

        /* Define a box, which is 3 blocks around the door. */
        AABB box = new AABB(new BlockPos(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3), new BlockPos(pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3));
        List<Player> playerList = world.getEntitiesOfClass(Player.class, box);

        /* If there's at least 1 player within the 3 block range, open the door */
        if (playerList.size() > 0) {
            if (!state.getValue(OPEN)) {
                world.playSound(null, pos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1, 1);
            }
            world.setBlock(pos, state.setValue(OPEN, true), 10);
        } else {
            /* Otherwise, close it. */
            if (state.getValue(OPEN)) {
                world.playSound(null, pos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1, 1);
            }
            world.setBlock(pos, state.setValue(OPEN, false), 10);
        }

        /* Schedule the check itself again to run after 5 ticks */
        Utilities.scheduleBlockTick(world, pos, state.getBlock(), 5);
    }
}