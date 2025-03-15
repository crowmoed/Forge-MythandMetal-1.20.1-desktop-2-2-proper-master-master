package net.pinto.mythandmetal.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MythandMetal.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MYTHANDMETALCRTAB = CREATIVE_MODE_TABS.register("mythandmetalcrtab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EXPLOSIVESWORD.get()))
                    .title(Component.translatable("creativetab.mythandmetalcrtab"))
                    .displayItems((pParameters, output) -> {
                        output.accept(ModBlocks.ENCHANTED_LEAVES.get());
                        output.accept(ModBlocks.ASH_LEAVES.get());

                        output.accept(ModBlocks.ASH_LOG.get());
                        output.accept(ModBlocks.ASHBURNT_LOG.get());

                        output.accept(ModBlocks.ENCHANTED_GRASS.get());
                        output.accept(ModBlocks.ASH_GRASS.get());
                        output.accept(ModBlocks.ASH_DIRT.get());


                        output.accept(ModBlocks.ASH_SAPLING.get());
                        output.accept(ModBlocks.ENCHANTED_SAPLING.get());

                        output.accept(ModItems.EXPLOSIVESWORD.get());

                        output.accept(ModItems.UPGRADE_INGOT_1.get());
                        output.accept(ModItems.UPGRADE_INGOT_2.get());
                        output.accept(ModItems.UPGRADE_INGOT_3.get());
                        output.accept(ModItems.UPGRADE_INGOT_4.get());
                        output.accept(ModItems.UPGRADE_INGOT_5.get());
                        output.accept(ModItems.UPGRADE_INGOT_6.get());


                        output.accept(ModItems.UPGRADE_INGOTSHARD_1.get());
                        output.accept(ModItems.UPGRADE_INGOTSHARD_2.get());
                        output.accept(ModItems.UPGRADE_INGOTSHARD_3.get());
                        output.accept(ModItems.UPGRADE_INGOTSHARD_4.get());
                        output.accept(ModItems.UPGRADE_INGOTSHARD_5.get());
                        output.accept(ModItems.UPGRADE_INGOTSHARD_6.get());

                        output.accept(ModBlocks.MYTHRIL_ORE_T2.get());
                        output.accept(ModBlocks.MYTHRIL_ORE_T3.get());
                        output.accept(ModBlocks.MYTHRIL_ORE_T4.get());
                        output.accept(ModBlocks.MYTHRIL_ORE_T5.get());
                        output.accept(ModBlocks.MYTHRIL_ORE_T6.get());
                        output.accept(ModBlocks.MYTHRIL_ORE_T7.get());

                        output.accept(ModBlocks.TIER_ANVIL.get());





                        output.accept(ModBlocks.MOD_PORTAL.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
