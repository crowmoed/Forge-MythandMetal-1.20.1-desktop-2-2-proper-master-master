package net.pinto.mythandmetal.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.pinto.mythandmetal.block.ModBlocks;

public class CustomRotatingBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public CustomRotatingBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        boolean isTouchinglava = isNearAdjacentTolava(world, pos);
        boolean isTouchingBurnt = isNearAdjacentToBurnt(world, pos);
        if (isTouchinglava||isTouchingBurnt){
            world.setBlock(pos, ModBlocks.ASHBURNT_LOG.get().defaultBlockState(), 2);
        }
    }
        private boolean isNearAdjacentTolava (ServerLevel world, BlockPos pos){
            for (Direction direction  : Direction.values()) {
                BlockState neighborState = world.getBlockState(pos.relative(direction));
                if (neighborState.is(ModBlocks.ASHBURNT_LOG.get())) {

                    return true;
                }
            }
            return false;
        }

        private boolean isNearAdjacentToBurnt (ServerLevel world, BlockPos pos){
            int range =4;

            for (int ix = -range; ix <= range; ix++) {
                for (int iy = -range; iy <= range; iy++) {
                    for (int iz = -range; iz <= range; iz++) {
                        if (ix == 0 && iy == 0 && iz == 0) continue;

                        BlockPos neighborPos = pos.offset(ix, iy, iz);
                        BlockState neighborState = world.getBlockState(neighborPos);
                        if (neighborState.is(Blocks.LAVA)) {
                            return true;
                        }
                    }
                }
            }
            return false;
    }

    public BlockState rotate(BlockState pState, Rotation pRot) {
        return rotatePillar(pState, pRot);
    }

    public static BlockState rotatePillar(BlockState pState, Rotation pRotation) {
        switch (pRotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)pState.getValue(AXIS)) {
                    case X:
                        return pState.setValue(AXIS, Direction.Axis.Z);
                    case Z:
                        return pState.setValue(AXIS, Direction.Axis.X);
                    default:
                        return pState;
                }
            default:
                return pState;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(AXIS, pContext.getClickedFace().getAxis());
    }
}
