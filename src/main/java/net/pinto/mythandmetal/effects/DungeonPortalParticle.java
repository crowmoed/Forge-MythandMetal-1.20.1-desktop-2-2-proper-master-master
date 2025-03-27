package net.pinto.mythandmetal.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DungeonPortalParticle extends TextureSheetParticle {
    protected DungeonPortalParticle(ClientLevel level, double x, double y, double z,
                                    double motionX, double motionY, double motionZ) {
        super(level, x, y, z, motionX, motionY, motionZ);

        // Magical particle properties
        this.rCol = 0.8f;  // Soft purple-blue
        this.gCol = 0.4f;
        this.bCol = 1.0f;
        this.alpha = 0.7f;

        this.lifetime = 40;  // Particle duration
        this.gravity = 0.02f;

        // Slight swirling motion
        this.xd = motionX * 0.5;
        this.yd = motionY * 0.5 + 0.05;
        this.zd = motionZ * 0.5;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            DungeonPortalParticle particle = new DungeonPortalParticle(level, x, y, z, dx, dy, dz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}