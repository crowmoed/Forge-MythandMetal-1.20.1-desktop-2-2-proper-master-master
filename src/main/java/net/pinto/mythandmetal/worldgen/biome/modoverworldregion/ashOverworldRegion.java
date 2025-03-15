package net.pinto.mythandmetal.worldgen.biome.modoverworldregion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.pinto.mythandmetal.worldgen.biome.ModBiomes;
import terrablender.api.ParameterUtils.*;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class ashOverworldRegion extends Region {


    public ashOverworldRegion(ResourceLocation name, int weight)
    {
        super(name, RegionType.OVERWORLD,weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterPointListBuilder()
                .temperature(Temperature.span(Temperature.WARM,Temperature.HOT))
                .humidity(Humidity.span(Humidity.ARID,Humidity.DRY))
                        .continentalness(Continentalness.FAR_INLAND)
                                .erosion(Erosion.EROSION_1,Erosion.EROSION_3)
                                        .depth(Depth.SURFACE,Depth.FLOOR)
                                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                                        .build().forEach(point ->builder.add(point, ModBiomes.ASH_FOREST));
        builder.build().forEach(mapper);


    }
}
