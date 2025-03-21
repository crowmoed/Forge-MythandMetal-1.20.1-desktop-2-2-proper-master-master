package net.pinto.mythandmetal.entity.AshenWolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class AshenWolfRenderer extends WolfRenderer {
    private static final ResourceLocation WOLF_LOCATION = new ResourceLocation("mythandmetal:textures/entity/ashenwolf/wolfashen.png");
    private static final ResourceLocation WOLF_TAME_LOCATION = new ResourceLocation("mythandmetal:textures/entity/ashenwolf/wolfashen.png");
    private static final ResourceLocation WOLF_ANGRY_LOCATION = new ResourceLocation("mythandmetal:textures/entity/ashenwolf/wolfashen.png");

    public AshenWolfRenderer(EntityRendererProvider.Context p_174452_) {
        super(p_174452_);
        this.addLayer(new WolfCollarLayer(this));
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float getBob(Wolf pLivingBase, float pPartialTicks) {
        return pLivingBase.getTailAngle();
    }

    public void render(Wolf pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isWet()) {
            float f = pEntity.getWetShade(pPartialTicks);
            this.model.setColor(f, f, f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        if (pEntity.isWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }

    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(Wolf pEntity) {
        if (pEntity.isTame()) {
            return WOLF_TAME_LOCATION;
        } else {
            return pEntity.isAngry() ? WOLF_ANGRY_LOCATION : WOLF_LOCATION;
        }
    }
}
