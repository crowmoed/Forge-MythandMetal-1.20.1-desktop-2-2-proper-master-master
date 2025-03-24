package net.pinto.mythandmetal.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> MYTHANDMETAL_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(MythandMetal.MOD_ID, "mythandmetaldim"));
    public static final ResourceKey<Level> MYTHANDMETAL_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(MythandMetal.MOD_ID, "mythandmetaldim"));
    public static final ResourceKey<DimensionType> MYTHANDMETAL_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(MythandMetal.MOD_ID, "mythandmetal_type"));


    public static final ResourceKey<LevelStem> DUNGEON_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(MythandMetal.MOD_ID, "dungeondim"));
    public static final ResourceKey<Level> DUNGEON_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(MythandMetal.MOD_ID, "dungeondim"));
    public static final ResourceKey<DimensionType> DUNGEON_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(MythandMetal.MOD_ID, "dungeon_type"));











    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(MYTHANDMETAL_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -64, // minY
                512, // height
                512, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));


        context.register(DUNGEON_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                false, // bedWorks
                false, // respawnAnchorWorks
                -64, // minY
                512, // height
                512, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.ASH_FOREST)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.ASH_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.ENCHANTED_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.METALPLAINS))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.MYTHANDMETAL_DIM_TYPE), noiseBasedChunkGenerator);

        context.register(MYTHANDMETAL_KEY, stem);









        NoiseBasedChunkGenerator lavawrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.DUNGEON)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD));

        LevelStem lavastem = new LevelStem(dimTypes.getOrThrow(ModDimensions.DUNGEON_DIM_TYPE), lavawrappedChunkGenerator);

        context.register(DUNGEON_KEY, lavastem);


    }









}
