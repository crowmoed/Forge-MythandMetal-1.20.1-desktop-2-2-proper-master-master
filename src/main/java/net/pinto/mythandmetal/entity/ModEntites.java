package net.pinto.mythandmetal.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.entity.AshenWolf.AshenWolf;

public class ModEntites {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MythandMetal.MOD_ID);

    public static final RegistryObject<EntityType<AshenWolf>> ASHENWOLF =
            ENTITY_TYPES.register("ashenwolf", ()-> EntityType.Builder.of(AshenWolf::new, MobCategory.CREATURE)
                    .sized(1.5f,1.5f).build("ashenwolf"));

    public static void  register(IEventBus eventBus)
    {
        ENTITY_TYPES.register(eventBus);
    }



}
