package com.jsblock;

import mtr.RegistryObject;
import net.minecraft.core.particles.SimpleParticleType;

public interface Particles {
    RegistryObject<SimpleParticleType> LIGHT_BLOCK = new RegistryObject<>(() -> new SimpleParticleType(true) {
    });
}