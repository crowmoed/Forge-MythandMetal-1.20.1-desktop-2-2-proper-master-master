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
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.pinto.mythandmetal.block.SavePortalData;
import net.pinto.mythandmetal.block.customEntity.ModLavaDungeonPortalDoorBlockEntity;
import net.pinto.mythandmetal.worldgen.dimension.ModDimensions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Properties;


public class ModLavaDungeonPortalDoor extends DirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING; // Define the FACING property
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<DoorHingeSide> SIDE = BlockStateProperties.DOOR_HINGE;

    public ModLavaDungeonPortalDoor(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH) // Default facing direction
                .setValue(HALF, DoubleBlockHalf.LOWER) // Default to the lower half
                .setValue(SIDE, DoorHingeSide.LEFT)); // Default hinge side
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SIDE); // Add FACING, HALF, and SIDE properties
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        // Ensure the block can be placed (upper block space is free)
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection()) // Set the FACING property
                    .setValue(HALF, DoubleBlockHalf.LOWER); // Set the HALF property
        }

        return null; // Prevent placement if the upper space is occupied
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        Direction facing = state.getValue(FACING); // Get the facing direction
        DoorHingeSide side = state.getValue(SIDE); // Get the hinge side

        // Calculate the positions for the other three blocks
        BlockPos rightPos = pos.relative(facing.getClockWise());
        BlockPos upperPos = pos.above();
        BlockPos upperRightPos = rightPos.above();

        // Place the lower-right block
        level.setBlock(rightPos, state.setValue(HALF, DoubleBlockHalf.LOWER).setValue(SIDE, side), 3);

        // Place the upper-left block
        level.setBlock(upperPos, state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(SIDE, side), 3);

        // Place the upper-right block
        level.setBlock(upperRightPos, state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(SIDE, side), 3);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf half = state.getValue(HALF); // Get the half (lower or upper)
        DoorHingeSide side = state.getValue(SIDE); // Get the hinge side (left or right)
        Direction facing = state.getValue(FACING); // Get the facing direction

        // Calculate the positions for the other three blocks
        BlockPos rightPos = pos.relative(facing.getClockWise());
        BlockPos upperPos = pos.above();
        BlockPos upperRightPos = rightPos.above();

        // Break all parts of the structure
        if (half == DoubleBlockHalf.LOWER) {
            // If breaking the lower block, break the upper blocks as well
            level.destroyBlock(rightPos, !player.isCreative());
            level.destroyBlock(upperPos, !player.isCreative());
            level.destroyBlock(upperRightPos, !player.isCreative());
        } else {
            // If breaking the upper block, break the lower blocks as well
            level.destroyBlock(pos.below(), !player.isCreative());
            level.destroyBlock(rightPos.below(), !player.isCreative());
            level.destroyBlock(rightPos, !player.isCreative());
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DoubleBlockHalf half = state.getValue(HALF); // Get the half (lower or upper)
        BlockPos belowPos = pos.below(); // Get the block below
        BlockState belowState = level.getBlockState(belowPos); // Get the state of the block below

        // Lower half must be placed on a solid surface
        if (half == DoubleBlockHalf.LOWER) {
            return belowState.isFaceSturdy(level, belowPos, Direction.UP);
        }

        // Upper half must have the lower half as the same block
        return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModLavaDungeonPortalDoorBlockEntity(pPos, pState); // Create a new block entity instance
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

    // Other methods (handlePortalOverworld, placementhelper, placelavadungeon, etc.) remain unchanged

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
                        data.setDungeonlava(data.getDungeonlava() + 1);

                    }
                    targetPortalPos = new BlockPos(placementhelper(blockEntity), 0, 0); // Fixed position in the modded dimension
                    serverPlayer.getPersistentData().putIntArray("portalPositiondungeonlava", new int[]{portalBlockPos.getX(), portalBlockPos.getY(), portalBlockPos.getZ()});
                    serverPlayer.getPersistentData().putString("fromdimension", player.level().dimension() + "");


                    if (blockEntity.isNotaccessed()) {
                        System.out.println("true");



                        placelavadungeon();
                        ResourceLocation structure = new ResourceLocation("mythandmetal", "modstructures/spawnroomdungeon");
                        BlockPos placeposition = new BlockPos(targetPortalPos.getX() , targetPortalPos.getY()-1, targetPortalPos.getZ());
                        placePortalTemplate(targetDimension, structure, placeposition, Rotation.NONE, Mirror.NONE, 1.0F, 0);
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

    private void placelavadungeon() {

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