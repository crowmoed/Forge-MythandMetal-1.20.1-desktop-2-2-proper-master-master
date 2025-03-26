package net.pinto.mythandmetal.renderer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.pinto.mythandmetal.block.customEntity.ModDungeonPortalDoorBlockEntity;
import net.pinto.mythandmetal.block.customEntity.MyBlockEntityTypes;
import net.pinto.mythandmetal.entity.ModEntites;

import java.util.Map;

public class BlockEntityRenderer {
    private static final Map<BlockEntityType<?>, BlockEntityRendererProvider<?>> PROVIDERS = new java.util.concurrent.ConcurrentHashMap<>();

    public static <T extends BlockEntity> void register(BlockEntityType<? extends T> pType, BlockEntityRendererProvider<T> pRenderProvider) {
        PROVIDERS.put(pType, pRenderProvider);
    }

    public static Map<BlockEntityType<?>, net.minecraft.client.renderer.blockentity.BlockEntityRenderer<?>> createEntityRenderers(BlockEntityRendererProvider.Context pContext) {
        ImmutableMap.Builder<BlockEntityType<?>, net.minecraft.client.renderer.blockentity.BlockEntityRenderer<?>> builder = ImmutableMap.builder();
        PROVIDERS.forEach((p_258150_, p_258151_) -> {
            try {
                builder.put(p_258150_, p_258151_.create(pContext));
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to create model for " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(p_258150_), exception);
            }
        });
        return builder.build();
    }

    static {
        register(MyBlockEntityTypes.LAVA_MOD_PORTAL.get(), DungeonPortalRenderer::new);

    }


}
