package net.pinto.mythandmetal.block.custom;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ResourceLocationException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.BlockHitResult;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.SavePortalData;
import net.pinto.mythandmetal.worldgen.dimension.ModDimensions;

import java.util.Optional;


public class ModPortalBlock extends Block {

    public ModPortalBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.canChangeDimensions()) {
            try {
                handlePortaloverworld(pPlayer, pPos);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }




    private void handlePortaloverworld(Entity player, BlockPos portalBlockPos) throws CommandSyntaxException {
        if (player.level() instanceof ServerLevel currentLevel) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            MinecraftServer minecraftServer = currentLevel.getServer();
            SavePortalData data = SavePortalData.get(minecraftServer);

            ResourceKey<Level> targetDimensionKey = player.level().dimension() == ModDimensions.MYTHANDMETAL_LEVEL_KEY
                    ? Level.OVERWORLD
                    : ModDimensions.MYTHANDMETAL_LEVEL_KEY;

            ServerLevel targetDimension = minecraftServer.getLevel(targetDimensionKey);

            if (targetDimension != null && !player.isPassenger()) {
                serverPlayer.changeDimension(targetDimension);

                BlockPos targetPortalPos;
                if (targetDimensionKey == ModDimensions.MYTHANDMETAL_LEVEL_KEY) {
                    targetPortalPos = new BlockPos(0, portalBlockPos.getY(), 0); // Fixed position in the modded dimension
                    serverPlayer.getPersistentData().putIntArray("portalPosition", new int[]{portalBlockPos.getX(), portalBlockPos.getY(), portalBlockPos.getZ()});
                    targetPortalPos = ensureSafePortalLocation(targetDimension, targetPortalPos);
                    if(data.getMyVariable() ==0){
                        ResourceLocation structure =  new ResourceLocation("mythandmetal", "modstructures/nexus");
                        BlockPos placeposition = new BlockPos(targetPortalPos.getX()-3,targetPortalPos.getY(),targetPortalPos.getZ()-4);
                        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);                    }
                    data.setMyVariable(1);
                    serverPlayer.teleportTo(
                            targetDimension,
                            targetPortalPos.getX() + 0.5, // Center the player on the block
                            targetPortalPos.getY()+1,
                            targetPortalPos.getZ() + 0.5,
                            player.getYRot(),
                            player.getXRot());
                } else {
                    int[] savedPortalPos = serverPlayer.getPersistentData().getIntArray("portalPosition");
                    targetPortalPos = new BlockPos(savedPortalPos[0]+1, savedPortalPos[1], savedPortalPos[2]);

                    serverPlayer.teleportTo(
                            targetDimension,
                            targetPortalPos.getX() + 0.5, // Center the player on the block
                            targetPortalPos.getY()+1,
                            targetPortalPos.getZ() + 0.5,
                            player.getYRot(),
                            player.getXRot());
                }



            }
        }
    }


    private BlockPos ensureSafePortalLocation(ServerLevel targetDimension, BlockPos portalPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(portalPos.getX(), 255, portalPos.getZ());

        while (mutablePos.getY() >= 0) {
            if ((targetDimension.getBlockState(mutablePos).getBlock() instanceof Block)&& (targetDimension.getBlockState(mutablePos).getBlock() != Blocks.AIR)&& (targetDimension.getBlockState(mutablePos).getBlock() != Blocks.VOID_AIR&& (targetDimension.getBlockState(mutablePos).getBlock() != Blocks.CAVE_AIR))) {

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
                structureplacesettings.clearProcessors().addProcessor(new BlockRotProcessor(pIntegrity)).setRandom(StructureBlockEntity.createRandom((long)pSeed));
            }

            boolean flag = structuretemplate.placeInWorld(serverlevel, pPos, pPos, structureplacesettings, StructureBlockEntity.createRandom((long)pSeed), 2);
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
