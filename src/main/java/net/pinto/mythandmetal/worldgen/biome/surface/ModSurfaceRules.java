package net.pinto.mythandmetal.worldgen.biome.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.pinto.mythandmetal.block.ModBlocks;
import net.pinto.mythandmetal.worldgen.biome.ModBiomes;

public class ModSurfaceRules {


    private static final SurfaceRules.RuleSource ASHDIRT = makeStateRule(ModBlocks.ASH_DIRT.get());
    private static final SurfaceRules.RuleSource ASHGRASS_BLOCK = makeStateRule(ModBlocks.ASH_GRASS.get());
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource ENCHANTEDGRASS = makeStateRule(ModBlocks.ENCHANTED_GRASS.get());


    public static SurfaceRules.RuleSource makeRules()
    {


        return SurfaceRules.sequence(

                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ASH_FOREST),
                    SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, ASHGRASS_BLOCK),
                            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, ASHDIRT)
                    )),

                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ENCHANTED_FOREST),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, ENCHANTEDGRASS),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT)
                        )
        ));
    }


    private static  SurfaceRules.RuleSource makeStateRule(Block block)
    {
        return SurfaceRules.state(block.defaultBlockState());
    }










}
