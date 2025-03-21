package net.pinto.mythandmetal.block.custom;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.BlockHitResult;
import net.pinto.mythandmetal.block.SavePortalData;
import net.pinto.mythandmetal.block.customEntity.ModLavaDungeonPortalDoorBlockEntity;
import net.pinto.mythandmetal.worldgen.dimension.ModDimensions;

import javax.annotation.Nullable;
import java.util.Optional;


public class ModLavaDungeonPortalDoor extends DirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<DoorHingeSide> SIDE = BlockStateProperties.DOOR_HINGE;

    public ModLavaDungeonPortalDoor(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(SIDE, DoorHingeSide.LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SIDE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection())
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        Direction facing = state.getValue(FACING);
        DoorHingeSide side = state.getValue(SIDE);

        BlockPos rightPos = pos.relative(facing.getClockWise());
        BlockPos upperPos = pos.above();
        BlockPos upperRightPos = rightPos.above();



        level.setBlock(rightPos, state.setValue(HALF, DoubleBlockHalf.LOWER).setValue(SIDE, side), 3);


        level.setBlock(upperPos, state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(SIDE, side), 3);

        level.setBlock(upperRightPos, state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(SIDE, side), 3);




    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf half = state.getValue(HALF);
        DoorHingeSide side = state.getValue(SIDE);
        Direction facing = state.getValue(FACING);

        BlockPos rightPos = pos.relative(facing.getClockWise());
        BlockPos upperPos = pos.above();
        BlockPos upperRightPos = rightPos.above();


        BlockPos opprightpos = new BlockPos(rightPos.getX()+2,rightPos.getY(),rightPos.getZ());
        BlockPos upperRightPosopp = new BlockPos(upperRightPos.getX()+2,upperRightPos.getY(),upperRightPos.getZ());


        BlockPos opprightposz = new BlockPos(rightPos.getX()-2,rightPos.getY(),rightPos.getZ());
        BlockPos upperRightPosoppz = new BlockPos(upperRightPos.getX()-2,upperRightPos.getY(),upperRightPos.getZ());


        BlockPos opprightposz2 = new BlockPos(rightPos.getX(),rightPos.getY(),rightPos.getZ()-2);
        BlockPos upperRightPosoppz2 = new BlockPos(upperRightPos.getX(),upperRightPos.getY(),upperRightPos.getZ()-2);


        BlockPos opprightposz22 = new BlockPos(rightPos.getX(),rightPos.getY(),rightPos.getZ()+2);
        BlockPos upperRightPosoppz22 = new BlockPos(upperRightPos.getX(),upperRightPos.getY(),upperRightPos.getZ()+2);


        if (half == DoubleBlockHalf.LOWER&&(level.getBlockState(rightPos).getBlock() instanceof ModLavaDungeonPortalDoor)) {
            level.destroyBlock(rightPos, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPos, !player.isCreative());
        } else if (level.getBlockState(rightPos).getBlock() instanceof ModLavaDungeonPortalDoor){
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(rightPos.below(), !player.isCreative());
            level.destroyBlock(rightPos, !player.isCreative());
        }
        else if (half == DoubleBlockHalf.LOWER&&(level.getBlockState(opprightpos).getBlock() instanceof ModLavaDungeonPortalDoor)) {
            level.destroyBlock(opprightpos, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPosopp, !player.isCreative());

        } else if (level.getBlockState(opprightpos).getBlock() instanceof ModLavaDungeonPortalDoor){
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(opprightpos.below(), !player.isCreative());
            level.destroyBlock(opprightpos, !player.isCreative());
        }



        else if (half == DoubleBlockHalf.LOWER&&(level.getBlockState(opprightposz).getBlock() instanceof ModLavaDungeonPortalDoor)) {
            level.destroyBlock(opprightposz, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPosoppz, !player.isCreative());

        } else if (level.getBlockState(opprightposz).getBlock() instanceof ModLavaDungeonPortalDoor){
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(opprightposz.below(), !player.isCreative());
            level.destroyBlock(opprightposz, !player.isCreative());
        }


        else if (half == DoubleBlockHalf.LOWER&&(level.getBlockState(opprightposz2).getBlock() instanceof ModLavaDungeonPortalDoor)) {
            level.destroyBlock(opprightposz2, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPosoppz2, !player.isCreative());

        } else if (level.getBlockState(opprightposz2).getBlock() instanceof ModLavaDungeonPortalDoor){
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(opprightposz2.below(), !player.isCreative());
            level.destroyBlock(opprightposz2, !player.isCreative());
        }


        else if (half == DoubleBlockHalf.LOWER&&(level.getBlockState(opprightposz22).getBlock() instanceof ModLavaDungeonPortalDoor)) {
            level.destroyBlock(opprightposz22, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPosoppz22, !player.isCreative());

        } else if (level.getBlockState(opprightposz22).getBlock() instanceof ModLavaDungeonPortalDoor){
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(opprightposz22.below(), !player.isCreative());
            level.destroyBlock(opprightposz22, !player.isCreative());
        }


        /*if(level.getBlockState(opprightpos).getBlock() instanceof ModLavaDungeonPortalDoor){
            System.out.println("working");
            level.destroyBlock(opprightpos, !player.isCreative());
            level.destroyBlock(upperPosopp, !player.isCreative());
            level.destroyBlock(upperRightPosopp, !player.isCreative());
        }


        if (half == DoubleBlockHalf.LOWER) {
            level.destroyBlock(rightPos, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPos, !player.isCreative());
        } else {
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(rightPos.below(), !player.isCreative());
            level.destroyBlock(rightPos, !player.isCreative());
        }*/

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (half == DoubleBlockHalf.LOWER) {
            return belowState.isFaceSturdy(level, belowPos, Direction.UP);
        }

        return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModLavaDungeonPortalDoorBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.canChangeDimensions() && pLevel.getBlockEntity(pPos) instanceof ModLavaDungeonPortalDoorBlockEntity blockEntity) {
            try {
                handlePortalOverworld(pPlayer, pPos, blockEntity);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }


    private void handlePortalOverworld(Entity player, BlockPos portalBlockPos,ModLavaDungeonPortalDoorBlockEntity blockEntity) throws CommandSyntaxException {
        if (player.level() instanceof ServerLevel currentLevel) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            MinecraftServer minecraftServer = currentLevel.getServer();
            SavePortalData data = SavePortalData.get(minecraftServer);

            ResourceKey<Level> targetDimensionKey = player.level().dimension() == ModDimensions.LAVADUNGEON_LEVEL_KEY
                    ? Level.OVERWORLD
                    : ModDimensions.LAVADUNGEON_LEVEL_KEY;

            ServerLevel targetDimension = minecraftServer.getLevel(targetDimensionKey);

            if (targetDimension != null && !player.isPassenger()) {
                serverPlayer.changeDimension(targetDimension);

                BlockPos targetPortalPos;
                if (targetDimensionKey == ModDimensions.LAVADUNGEON_LEVEL_KEY) {
                    if (blockEntity.isNotaccessed()) {
                        blockEntity.setAccessnumber(data.getDungeonlava() );
                        Direction facing = blockEntity.getBlockState().getValue(FACING);



                        BlockPos belowpos = portalBlockPos.below();
                        BlockPos belowposr = new BlockPos(belowpos.getX()-1,belowpos.getY(),belowpos.getZ());
                        BlockPos belowposl = new BlockPos(belowpos.getX()+1,belowpos.getY(),belowpos.getZ());
                        BlockPos belowposz = new BlockPos(belowpos.getX(),belowpos.getY(),belowpos.getZ()-1);
                        BlockPos belowposz2 = new BlockPos(belowpos.getX(),belowpos.getY(),belowpos.getZ()+1);



                        BlockPos rightPos = portalBlockPos.relative(facing.getClockWise());
                        BlockPos upperPos = portalBlockPos.above();
                        BlockPos upperRightPos = rightPos.above();
                        currentLevel.getBlockState(portalBlockPos);

                        // here
                        BlockPos opprightpos = new BlockPos(rightPos.getX()+2,rightPos.getY(),rightPos.getZ());
                        BlockPos upperRightPosopp = new BlockPos(upperRightPos.getX()+2,upperRightPos.getY(),upperRightPos.getZ());


                        BlockPos opprightposz = new BlockPos(rightPos.getX()-2,rightPos.getY(),rightPos.getZ());
                        BlockPos upperRightPosoppz = new BlockPos(upperRightPos.getX()-2,upperRightPos.getY(),upperRightPos.getZ());


                        BlockPos opprightposz2 = new BlockPos(rightPos.getX(),rightPos.getY(),rightPos.getZ()-2);
                        BlockPos upperRightPosoppz2 = new BlockPos(upperRightPos.getX(),upperRightPos.getY(),upperRightPos.getZ()-2);


                        BlockPos opprightposz22 = new BlockPos(rightPos.getX(),rightPos.getY(),rightPos.getZ()+2);
                        BlockPos upperRightPosoppz22 = new BlockPos(upperRightPos.getX(),upperRightPos.getY(),upperRightPos.getZ()+2);
                        //stop
                        if (currentLevel.getBlockEntity(belowposz2) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                        if (currentLevel.getBlockEntity(belowposz) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                        if (currentLevel.getBlockEntity(belowposl) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}

                        if (currentLevel.getBlockEntity(belowposr) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}

                            if (currentLevel.getBlockEntity(rightPos) instanceof ModLavaDungeonPortalDoorBlockEntity rightBlockEntity) {
                                rightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                                rightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(upperPos) instanceof ModLavaDungeonPortalDoorBlockEntity upperBlockEntity) {
                                upperBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                                upperBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(upperRightPos) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                                upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                                upperRightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(belowpos) instanceof ModLavaDungeonPortalDoorBlockEntity upperBlockEntity) {
                            upperBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(opprightpos) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(upperRightPosopp) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(opprightposz) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(upperRightPosoppz) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                        if (currentLevel.getBlockEntity(opprightposz22) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                        if (currentLevel.getBlockEntity(upperRightPosoppz22) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(opprightposz2) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}
                            if (currentLevel.getBlockEntity(upperRightPosoppz2) instanceof ModLavaDungeonPortalDoorBlockEntity upperRightBlockEntity) {
                            upperRightBlockEntity.setAccessnumber(blockEntity.getAccessNumber());
                            upperRightBlockEntity.setNotaccessed(false);}







                        data.setDungeonlava(data.getDungeonlava() + 1);
                    }
                    targetPortalPos = new BlockPos(placementhelper(blockEntity), 0, 0); // Fixed position in the modded dimension
                    serverPlayer.getPersistentData().putIntArray("portalPositiondungeonlava", new int[]{portalBlockPos.getX(), portalBlockPos.getY(), portalBlockPos.getZ()});
                    serverPlayer.getPersistentData().putString("fromdimension", player.level().dimension() + "");


                    if (blockEntity.isNotaccessed()) {



                        placelavadungeon(targetPortalPos , targetDimension);

                        blockEntity.setNotaccessed(false);


                    }

                    serverPlayer.teleportTo(
                            targetDimension,
                            targetPortalPos.getX() + 8,
                            targetPortalPos.getY() + 1,
                            targetPortalPos.getZ() + 3.5,
                            player.getYRot(),
                            player.getXRot());
                } else {
                    int[] savedPortalPos = serverPlayer.getPersistentData().getIntArray("portalPositiondungeonlava");
                    String fromdimension = serverPlayer.getPersistentData().getString("fromdimension");

                    targetPortalPos = new BlockPos(savedPortalPos[0] + 1, savedPortalPos[1], savedPortalPos[2]);

                    serverPlayer.teleportTo(
                            handfromdimension(fromdimension, minecraftServer),
                            targetPortalPos.getX() + 0.5,
                            targetPortalPos.getY() + 1,
                            targetPortalPos.getZ() + 0.5,
                            player.getYRot(),
                            player.getXRot());
                }
            }
        }
    }

    private int placementhelper(ModLavaDungeonPortalDoorBlockEntity blockEntity) {
        return blockEntity.getAccessNumber() % 2 == 0 ? 500 * blockEntity.getAccessNumber() : -500 * blockEntity.getAccessNumber();
    }

    private void placelavadungeon(BlockPos targetPortalPos, ServerLevel targetDimension) throws CommandSyntaxException {
        ResourceLocation structure = new ResourceLocation("mythandmetal", "modstructures/spawnroomdungeon");
        BlockPos placeposition = new BlockPos(targetPortalPos.getX() , targetPortalPos.getY()-1, targetPortalPos.getZ());
        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);
    }







    private ServerLevel handfromdimension(String bruh, MinecraftServer level) {
        if (bruh.equals("ResourceKey[minecraft:dimension / minecraft:overworld]"))
            return level.getLevel(Level.OVERWORLD);

        return level.getLevel(ModDimensions.MYTHANDMETAL_LEVEL_KEY);
    }



















    private BlockPos ensureSafePortalLocation(ServerLevel targetDimension, BlockPos portalPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(portalPos.getX(), 255, portalPos.getZ());

        while (mutablePos.getY() >= 0) {
            if ((targetDimension.getBlockState(mutablePos).getBlock() instanceof Block) && (targetDimension.getBlockState(mutablePos).getBlock() != Blocks.AIR) && (targetDimension.getBlockState(mutablePos).getBlock() != Blocks.VOID_AIR && (targetDimension.getBlockState(mutablePos).getBlock() != Blocks.CAVE_AIR))) {

                BlockPos abovePos = mutablePos.above();
                if (targetDimension.getBlockState(abovePos).isAir()) {
                    return abovePos;
                }
            }
            mutablePos.move(0, -1, 0);
        }
        mutablePos.move(0, 1, 0);

        return mutablePos;
    }

    private static final DynamicCommandExceptionType ERROR_TEMPLATE_INVALID = new DynamicCommandExceptionType((p_214582_) -> {
        return Component.translatable("commands.place.template.invalid", p_214582_);
    });

    private static final SimpleCommandExceptionType ERROR_TEMPLATE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.template.failed"));

    public static int placePortalTemplate(ServerLevel serverlevel2, ResourceLocation pTemplate, BlockPos pPos, Rotation pRotation, Mirror pMirror, float pIntegrity, int pSeed) throws CommandSyntaxException {
        ServerLevel serverlevel = serverlevel2;
        StructureTemplateManager structuretemplatemanager = serverlevel.getStructureManager();

        Optional<StructureTemplate> optional;
        try {
            optional = structuretemplatemanager.get(pTemplate);
        } catch (ResourceLocationException resourcelocationexception) {
            throw ERROR_TEMPLATE_INVALID.create(pTemplate);
        }

        if (optional.isEmpty()) {
            throw ERROR_TEMPLATE_INVALID.create(pTemplate);
        } else {
            StructureTemplate structuretemplate = optional.get();
            checkLoaded(serverlevel, new ChunkPos(pPos), new ChunkPos(pPos.offset(structuretemplate.getSize())));
            StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setMirror(pMirror).setRotation(pRotation);
            if (pIntegrity < 1.0F) {
                structureplacesettings.clearProcessors().addProcessor(new BlockRotProcessor(pIntegrity)).setRandom(StructureBlockEntity.createRandom((long) pSeed));
            }

            boolean flag = structuretemplate.placeInWorld(serverlevel, pPos, pPos, structureplacesettings, StructureBlockEntity.createRandom((long) pSeed), 2);
            if (!flag) {
                throw ERROR_TEMPLATE_FAILED.create();
            } else {
                return 1;
            }
        }
    }

    private static void checkLoaded(ServerLevel pLevel, ChunkPos pStart, ChunkPos pEnd) {
        ChunkPos.rangeClosed(pStart, pEnd).forEach((chunkPos) -> {
            if (!pLevel.isLoaded(chunkPos.getWorldPosition())) {
                // Force load the chunk if it's not loaded
                pLevel.getChunk(chunkPos.x, chunkPos.z);
            }
        });
    }
}