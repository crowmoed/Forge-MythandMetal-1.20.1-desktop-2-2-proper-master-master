package net.pinto.mythandmetal.worldgen.biome;


import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {


    public static final ResourceKey<PlacedFeature> ASH_PLACED_KEY = registerKey("ash_placed");
    public static final ResourceKey<PlacedFeature> ENCHANTED_PLACED_KEY = registerKey("enchanted_placed");
    public static final ResourceKey<PlacedFeature> MAGMA_ROCK = registerKey("magma_rock");
    public static final ResourceKey<PlacedFeature> LAVA_ASH = registerKey("lava_ash");






    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> holder2 = holdergetter.getOrThrow(ModConfiguredFeatures.MAGMA_ROCK);
        Holder<ConfiguredFeature<?, ?>> holder6 = holdergetter.getOrThrow(ModConfiguredFeatures.LAVA_ASH_KEY);


        PlacementUtils.register(context, LAVA_ASH, holder6, RarityFilter.onAverageOnceEvery(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        register(context, ASH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ASH_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 2),
                        ModBlocks.ASH_SAPLING.get()));

        register(context, ENCHANTED_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ENCHANTED_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 2),
                        ModBlocks.ENCHANTED_SAPLING.get()));

        PlacementUtils.register(context, MAGMA_ROCK, holder2, CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());




    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MythandMetal.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}