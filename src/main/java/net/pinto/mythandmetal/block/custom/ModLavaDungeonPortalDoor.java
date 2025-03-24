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
import net.pinto.mythandmetal.block.customEntity.ModDungeonPortalDoorBlockEntity;

import net.pinto.mythandmetal.worldgen.dimension.ModDimensions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModDungeonPortalDoorBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.canChangeDimensions() && pLevel.getBlockEntity(pPos) instanceof ModDungeonPortalDoorBlockEntity blockEntity) {
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


    private void handlePortalOverworld(Entity player, BlockPos portalBlockPos2,ModDungeonPortalDoorBlockEntity blockEntity) throws CommandSyntaxException {
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
                        BlockPos portalBlockPos = blockEntity.getBlockPos();

// Calculate all positions to check within a 2-block cube
                        List<BlockPos> allPositions = new ArrayList<>();

// Check 4 vertical layers: below, same, upper, and upper+1
                        for (int yOffset = -1; yOffset <= 2; yOffset++) {
                            // Check 5x5 grid horizontally for each layer
                            for (int dx = -2; dx <= 2; dx++) {
                                for (int dz = -2; dz <= 2; dz++) {
                                    // Skip the portal's own position in its main layer
                                    if (yOffset == 0 && dx == 0 && dz == 0) continue;

                                    // Limit to diamond-shaped area (Manhattan distance <= 2)
                                    if (Math.abs(dx) + Math.abs(dz) > 2) continue;

                                    allPositions.add(portalBlockPos.offset(dx, yOffset, dz));
                                }
                            }
                        }

// Update all relevant blocks
                        for (BlockPos checkPos : allPositions) {
                            if (currentLevel.getBlockEntity(checkPos) instanceof ModDungeonPortalDoorBlockEntity adjacentEntity) {
                                adjacentEntity.setAccessnumber(blockEntity.getAccessNumber());
                                adjacentEntity.setNotaccessed(false);
                            }}






                        data.setDungeonlava(data.getDungeonlava() + 1);
                    }
                    targetPortalPos = new BlockPos(placementhelper(blockEntity), 0, 0); // Fixed position in the modded dimension
                    serverPlayer.getPersistentData().putIntArray("portalPositiondungeonlava", new int[]{portalBlockPos2.getX(), portalBlockPos2.getY(), portalBlockPos2.getZ()});
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

    private int placementhelper(ModDungeonPortalDoorBlockEntity blockEntity) {
        return blockEntity.getAccessNumber() % 2 == 0 ? 500 * blockEntity.getAccessNumber() : -500 * blockEntity.getAccessNumber();
    }

    private void placelavadungeon(BlockPos targetPortalPos, ServerLevel targetDimension) throws CommandSyntaxException {
        ResourceLocation structure = new ResourceLocation("mythandmetal", "modstructures/spawnroomdungeon");
        BlockPos placeposition = new BlockPos(targetPortalPos.getX() , targetPortalPos.getY()-1, targetPortalPos.getZ());
        placehallway(targetPortalPos,targetDimension);
        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);
    }




    private void placehallway(BlockPos targetPortalPos, ServerLevel targetDimension) throws CommandSyntaxException {
        ResourceLocation structure = new ResourceLocation("mythandmetal", "modstructures/hallwaybedrockdungeon");
        BlockPos placeposition = new BlockPos(targetPortalPos.getX() , targetPortalPos.getY()-1, targetPortalPos.getZ());
        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);
        ResourceLocation structure2 = new ResourceLocation("mythandmetal", "modstructures/hallwaybrickdungeon");
        BlockPos placeposition2 = new BlockPos(targetPortalPos.getX() , targetPortalPos.getY()-1, targetPortalPos.getZ());
        placePortalTemplate(targetDimension, structure2, placeposition2, Rotation.NONE, Mirror.NONE, 1.0F, 0);
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