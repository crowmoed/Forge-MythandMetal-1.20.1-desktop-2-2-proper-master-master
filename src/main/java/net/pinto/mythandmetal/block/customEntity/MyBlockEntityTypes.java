package net.pinto.mythandmetal.block.customEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.block.ModBlocks;

public class MyBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "mythandmetal");

    public static final RegistryObject<BlockEntityType<ModLavaDungeonPortalDoorBlockEntity>> LAVA_MOD_PORTAL =
            BLOCK_ENTITY_TYPES.register("lava_mod_portal",
                    () -> BlockEntityType.Builder.of(ModLavaDungeonPortalDoorBlockEntity::new,
                            ModBlocks.LAVA_MOD_PORTAL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
