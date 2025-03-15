package net.pinto.mythandmetal.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.block.ModBlocks;
import net.pinto.mythandmetal.item.ModItems;

import java.util.Set;

public class ModBlockLootDrops extends BlockLootSubProvider {

    public ModBlockLootDrops() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.ENCHANTED_GRASS.get());
        this.dropSelf(ModBlocks.ASHBURNT_LOG.get());
        this.dropSelf(ModBlocks.ENCHANTED_LOG.get());
        this.dropSelf(ModBlocks.ASH_SAPLING.get());
        this.dropSelf(ModBlocks.ASH_DIRT.get());
        this.dropOther(ModBlocks.ASH_GRASS.get(), Blocks.DIRT);
        this.dropSelf(ModBlocks.ASH_LOG.get());
        this.dropSelf(ModBlocks.TIER_ANVIL.get());
        this.add(ModBlocks.ENCHANTED_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.ASH_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(ModBlocks.ASH_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.ASH_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));


        this.dropSelf(ModBlocks.ASH_SAPLING.get());
        this.dropSelf(ModBlocks.ENCHANTED_SAPLING.get());

        this.add(ModBlocks.MYTHRIL_ORE_T2.get(),
                block -> createCopperLikeOreDrops(ModBlocks.MYTHRIL_ORE_T2.get(), ModItems.UPGRADE_INGOTSHARD_1.get()));
        this.add(ModBlocks.MYTHRIL_ORE_T3.get(),
                block -> createCopperLikeOreDrops(ModBlocks.MYTHRIL_ORE_T3.get(), ModItems.UPGRADE_INGOTSHARD_2.get()));
        this.add(ModBlocks.MYTHRIL_ORE_T4.get(),
                block -> createCopperLikeOreDrops(ModBlocks.MYTHRIL_ORE_T4.get(), ModItems.UPGRADE_INGOTSHARD_3.get()));
        this.add(ModBlocks.MYTHRIL_ORE_T5.get(),
                block -> createCopperLikeOreDrops(ModBlocks.MYTHRIL_ORE_T5.get(), ModItems.UPGRADE_INGOTSHARD_4.get()));
        this.add(ModBlocks.MYTHRIL_ORE_T6.get(),
                block -> createCopperLikeOreDrops(ModBlocks.MYTHRIL_ORE_T6.get(), ModItems.UPGRADE_INGOTSHARD_5.get()));
        this.add(ModBlocks.MYTHRIL_ORE_T7.get(),
                block -> createCopperLikeOreDrops(ModBlocks.MYTHRIL_ORE_T7.get(), ModItems.UPGRADE_INGOTSHARD_6.get()));



    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }




    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
