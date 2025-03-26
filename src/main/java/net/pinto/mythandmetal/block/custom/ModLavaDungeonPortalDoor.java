package net.pinto.mythandmetal.block.custom;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.pinto.mythandmetal.block.SavePortalData;
import net.pinto.mythandmetal.block.customEntity.ModDungeonPortalDoorBlockEntity;
import net.pinto.mythandmetal.worldgen.dimension.ModDimensions;

import java.util.ArrayList;
import java.util.List;


public class ModLavaDungeonPortalDoor extends ModDungeonPortalDoor {

    public ModLavaDungeonPortalDoor(Properties pProperties) {
        super(pProperties);
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



    private void handlePortalOverworld(Entity player, BlockPos portalBlockPos2, ModDungeonPortalDoorBlockEntity blockEntity) throws CommandSyntaxException {
        if (player.level() instanceof ServerLevel currentLevel) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            MinecraftServer minecraftServer = currentLevel.getServer();
            SavePortalData data = SavePortalData.get(minecraftServer);

            ResourceKey<Level> targetDimensionKey = player.level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY
                    ? Level.OVERWORLD
                    : ModDimensions.DUNGEON_LEVEL_KEY;

            ServerLevel targetDimension = minecraftServer.getLevel(targetDimensionKey);

            if (targetDimension != null && !player.isPassenger()) {
                serverPlayer.changeDimension(targetDimension);

                BlockPos targetPortalPos;
                if (targetDimensionKey == ModDimensions.DUNGEON_LEVEL_KEY) {
                    if (blockEntity.isNotaccessed()) {
                        blockEntity.setAccessnumber(data.getDungeonlava() );
                        Direction facing = blockEntity.getBlockState().getValue(FACING);
                        BlockPos portalBlockPos = blockEntity.getBlockPos();

                        List<BlockPos> allPositions = new ArrayList<>();

                        for (int yOffset = -1; yOffset <= 2; yOffset++) {
                            for (int dx = -2; dx <= 2; dx++) {
                                for (int dz = -2; dz <= 2; dz++) {
                                    if (yOffset == 0 && dx == 0 && dz == 0) continue;

                                    if (Math.abs(dx) + Math.abs(dz) > 2) continue;

                                    allPositions.add(portalBlockPos.offset(dx, yOffset, dz));
                                }
                            }
                        }

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



                        placedungeon(targetPortalPos , targetDimension);

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



    private void placedungeon(BlockPos targetPortalPos, ServerLevel targetDimension) throws CommandSyntaxException {
        ResourceLocation structure = new ResourceLocation("mythandmetal", "modstructures/spawnroomdungeon");
        BlockPos placeposition = new BlockPos(targetPortalPos.getX() , targetPortalPos.getY()-1, targetPortalPos.getZ());
        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);
        placehallway(targetPortalPos,targetDimension);
    }




    private void placehallway(BlockPos targetPortalPos, ServerLevel targetDimension) throws CommandSyntaxException {
        ResourceLocation structure = new ResourceLocation("mythandmetal", "modstructures/hallwaybedrockdungeon");
        BlockPos placeposition = new BlockPos(targetPortalPos.getX()+3 , targetPortalPos.getY()-1, targetPortalPos.getZ()+15);
        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);

        ResourceLocation structure2 = new ResourceLocation("mythandmetal", "modstructures/hallwaybrickdungeon");
        BlockPos placeposition2 = new BlockPos(targetPortalPos.getX()+4 , targetPortalPos.getY(), targetPortalPos.getZ()+14);
        placePortalTemplate(targetDimension, structure2, placeposition2, Rotation.NONE, Mirror.NONE, 1.0F, 0);


    }

}