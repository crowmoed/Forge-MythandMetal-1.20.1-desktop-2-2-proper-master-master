package net.pinto.mythandmetal.effects;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.MythandMetal;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MythandMetal.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DUNGEON_PORTAL_PARTICLE =
            PARTICLE_TYPES.register("dungeon_portal_particle",
                    () -> new SimpleParticleType(true));

    public static void registerParticleFactories(IEventBus event) {
        PARTICLE_TYPES.register(event);
    }
}