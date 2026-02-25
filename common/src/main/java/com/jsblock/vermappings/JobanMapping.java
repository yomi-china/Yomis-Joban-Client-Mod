package com.jsblock.vermappings;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JobanMapping {
    public static void sendSoundToPlayer(Level world, ServerPlayer player, ResourceLocation name, SoundSource soundSource, BlockPos blockPos, float soundVolume) {
        final Vec3 pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Holder<SoundEvent> holder = Holder.direct(SoundEvent.createVariableRangeEvent(name));
        player.connection.send(new ClientboundSoundPacket(holder, soundSource, pos.x, pos.y, pos.z, soundVolume, 1, world.getRandom().nextLong()));
    }
}