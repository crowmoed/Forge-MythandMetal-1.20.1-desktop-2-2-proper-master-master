package net.pinto.mythandmetal.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.ModBlocks;
public class ModConfiguredFeatures {

public static final ResourceKey<ConfiguredFeature<?, ?>> MAGMA_ROCK = registerKey("magma_rock");
public static final ResourceKey<ConfiguredFeature<?, ?>> ASH_KEY = registerKey("ash_tree");

public static final ResourceKey<ConfiguredFeature<?, ?>> ENCHANTED_KEY = registerKey("enchanted_tree");
public static final ResourceKey<ConfiguredFeature<?, ?>> LAVA_ASH_KEY = registerKey("lake_ash");


public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

    register(context, MAGMA_ROCK, Feature.FOREST_ROCK, new BlockStateConfiguration(Blocks.MAGMA_BLOCK.defaultBlockState()));


    register(context, LAVA_ASH_KEY, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.LAVA.defaultBlockState()), BlockStateProvider.simple(Blocks.NETHERRACK.defaultBlockState())));

    register(context, ASH_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(ModBlocks.ASH_LOG.get()),
            new StraightTrunkPlacer(5, 4, 3)
            ,

            BlockStateProvider.simple(ModBlocks.ASH_LEAVES.get()),
            new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(2), 2),

            new TwoLayersFeatureSize(1, 0, 2)).build());

    register(context, ENCHANTED_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(ModBlocks.ENCHANTED_LOG.get()),
            new StraightTrunkPlacer(5, 4, 3)
            ,

            BlockStateProvider.simple(ModBlocks.ENCHANTED_LEAVES.get()),
            new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(2), 2),

            new TwoLayersFeatureSize(1, 0, 2)).build());
}

public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
    return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MythandMetal.MOD_ID, name));
}

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));}

}
