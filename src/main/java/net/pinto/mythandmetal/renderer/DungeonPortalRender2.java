package net.pinto.mythandmetal.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.pinto.mythandmetal.block.customEntity.ModDungeonPortalDoorBlockEntity;

public class DungeonPortalRender2  extends  DungeonPortalRenderer<ModDungeonPortalDoorBlockEntity> {
    public DungeonPortalRender2(BlockEntityRendererProvider.Context context) {
        super(context);
    }




    private static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/end_gateway_beam.png");



    protected float getOffsetUp() {
        return 1.0F;
    }

    protected float getOffsetDown() {
        return 0.0F;
    }

    protected RenderType renderType() {
        return RenderType.endGateway();
    }

    public int getViewDistance() {
        return 256;
    }
}
