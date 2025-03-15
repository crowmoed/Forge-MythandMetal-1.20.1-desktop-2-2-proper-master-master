package net.pinto.mythandmetal.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.worldgen.biome.ModBiomes;
import net.pinto.mythandmetal.worldgen.biome.ModConfiguredFeatures;
import net.pinto.mythandmetal.worldgen.biome.ModPlacedFeatures;
import net.pinto.mythandmetal.worldgen.dimension.ModDimensions;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {


    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem);


    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MythandMetal.MOD_ID));
    }

}
