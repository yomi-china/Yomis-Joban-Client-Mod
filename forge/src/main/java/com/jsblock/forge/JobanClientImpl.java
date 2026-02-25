package com.jsblock.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class JobanClientImpl {
    public static void registerParticle(SimpleParticleType particle, ParticleProvider<SimpleParticleType> provider) {
        Minecraft.getInstance().particleEngine.register(particle, provider);
    }
}
