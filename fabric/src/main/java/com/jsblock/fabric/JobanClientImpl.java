package com.jsblock.fabric;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class JobanClientImpl {
    public static void registerParticle(SimpleParticleType particle, ParticleProvider<SimpleParticleType> provider) {
        ParticleFactoryRegistry.getInstance().register(particle, provider);
    }
}
