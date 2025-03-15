package net.pinto.mythandmetal.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.ModBlocks;

public class CustVillager {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, MythandMetal.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MythandMetal.MOD_ID);

    public static final RegistryObject<PoiType> MYTHRIL_POI = POI_TYPES.register("mythril_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.TIER_ANVIL.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> MYTHRIL_TRADER =
            VILLAGER_PROFESSIONS.register("mythriltrader", () -> new VillagerProfession("mythriltrader",
                    holder -> holder.get() == MYTHRIL_POI.get(), holder -> holder.get() == MYTHRIL_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));



    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }



}
