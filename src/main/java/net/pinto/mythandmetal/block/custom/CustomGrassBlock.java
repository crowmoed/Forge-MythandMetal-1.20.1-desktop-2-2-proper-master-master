package net.pinto.mythandmetal.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.pinto.mythandmetal.block.ModBlocks;

public class CustomGrassBlock extends GrassBlock {
    public CustomGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {
            // Check if the custom grass block is covered by an opaque block
            if (isCovered(level, pos)) {
                // Convert the custom grass block into a dirt block
                level.setBlock(pos, ModBlocks.ASH_DIRT.get().defaultBlockState(), 3);
            } else {
                // Otherwise, try to spread to nearby dirt blocks
                if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos targetPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                        BlockState targetState = level.getBlockState(targetPos);
                        // Check if the target block is part of the dirt tag
                        if (targetState.is(BlockTags.DIRT)) {
                            // Check if the dirt block can be converted to custom grass
                            if (canPropagate(level, targetPos)) {
                                level.setBlock(targetPos, this.defaultBlockState(), 3); // Convert to custom grass block
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isCovered(LevelReader level, BlockPos pos) {
        // Check if there's an opaque block above the custom grass block
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        return aboveState.isSolidRender(level, abovePos);
    }

    private boolean canPropagate(LevelReader level, BlockPos pos) {
        // Check if there's no opaque block above the dirt block
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        return !aboveState.isSolidRender(level, abovePos);
    }

}
