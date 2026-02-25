package com.jsblock.particle;

import com.jsblock.Blocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Class for Light Particle
 * @author LX86
 * @since 1.1.2
 */
@Environment(EnvType.CLIENT)
public class LightBlockParticle extends TextureSheetParticle {
    private LightBlockParticle(ClientLevel world, double x, double y, double z, ItemLike item) {
        super(world, x, y, z);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(new ItemStack(item)).getParticleIcon());
        this.gravity = 0.0F;
        this.lifetime = 80;
        this.hasPhysics = false;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    public float getQuadSize(float scaleFactor) {
        return 0.5F;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider() {
        }

        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LightBlockParticle(world, x, y, z, Blocks.LIGHT_BLOCK.get().asItem());
        }
    }
}
