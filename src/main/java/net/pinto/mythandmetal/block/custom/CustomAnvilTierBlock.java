package net.pinto.mythandmetal.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.pinto.mythandmetal.menu.custom.CustomAnvilTierMenu;

import javax.annotation.Nullable;

public class CustomAnvilTierBlock extends FallingBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;



    private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);

    private static final VoxelShape MIDDLE_X = Block.box(5.0D, 2.0D, 4.0D, 11.0D, 3.0D, 12.0D);
    private static final VoxelShape MIDDLE_Z = Block.box(4.0D, 2.0D, 5.0D, 12.0D, 3.0D, 11.0D);

    private static final VoxelShape UPPER_BASE_X = Block.box(6.0D, 3.0D, 5.0D, 10.0D, 6.0D, 11.0D);
    private static final VoxelShape UPPER_BASE_Z = Block.box(5.0D, 3.0D, 6.0D, 11.0D, 6.0D, 10.0D);

    private static final VoxelShape TOP_X = Block.box(0.0D, 6.0D, 3.0D, 16.0D, 12.0D, 13.0D);
    private static final VoxelShape TOP_Z = Block.box(3.0D, 6.0D, 0.0D, 13.0D, 12.0D, 16.0D);

    // Combined directional shapes
    private static final VoxelShape X_SHAPE = Shapes.or(
            BASE,
            MIDDLE_Z,
            UPPER_BASE_Z,
            TOP_X
    );

    private static final VoxelShape Z_SHAPE = Shapes.or(
            BASE,
            MIDDLE_X,
            UPPER_BASE_X,
            TOP_Z
    );
        private static final Component CONTAINER_TITLE = Component.translatable("container.repair");
        private static final float FALL_DAMAGE_PER_DISTANCE = 2.0F;
        private static final int FALL_DAMAGE_MAX = 40;





        public CustomAnvilTierBlock(Properties pProperties) {
            super(pProperties);
            this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        }

        public BlockState getStateForPlacement(BlockPlaceContext pContext) {
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getClockWise());
        }

        public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
            if (pLevel.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
                pPlayer.awardStat(Stats.INTERACT_WITH_ANVIL);
                return InteractionResult.CONSUME;
            }
        }

        @Nullable
        public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
            return new SimpleMenuProvider((p_48785_, p_48786_, p_48787_) -> {
                return new CustomAnvilTierMenu(p_48785_, p_48786_, ContainerLevelAccess.create(pLevel, pPos));
            }, CONTAINER_TITLE);
        }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }


    protected void falling(FallingBlockEntity pFallingEntity) {
            pFallingEntity.setHurtsEntities(2.0F, 40);
        }

        public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, FallingBlockEntity pFallingBlock) {
            if (!pFallingBlock.isSilent()) {
                pLevel.levelEvent(1031, pPos, 0);
            }

        }

        public void onBrokenAfterFall(Level pLevel, BlockPos pPos, FallingBlockEntity pFallingBlock) {
            if (!pFallingBlock.isSilent()) {
                pLevel.levelEvent(1029, pPos, 0);
            }

        }

        public DamageSource getFallDamageSource(Entity pEntity) {
            return pEntity.damageSources().anvil(pEntity);
        }

        @Nullable
        public static BlockState damage(BlockState pState) {
            if (pState.is(Blocks.ANVIL)) {
                return Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(FACING, pState.getValue(FACING));
            } else {
                return pState.is(Blocks.CHIPPED_ANVIL) ? Blocks.DAMAGED_ANVIL.defaultBlockState().setValue(FACING, pState.getValue(FACING)) : null;
            }
        }


        public BlockState rotate(BlockState pState, Rotation pRot) {
            return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
        }

        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
            pBuilder.add(FACING);
        }

        public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
            return false;
        }

        public int getDustColor(BlockState pState, BlockGetter pReader, BlockPos pPos) {
            return pState.getMapColor(pReader, pPos).col;
        }
}
