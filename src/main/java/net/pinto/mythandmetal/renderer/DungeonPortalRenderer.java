package net.pinto.mythandmetal.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.customEntity.ModDungeonPortalDoorBlockEntity;
import net.pinto.mythandmetal.effects.ModParticleTypes;

import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class DungeonPortalRenderer implements BlockEntityRenderer<ModDungeonPortalDoorBlockEntity> {
    // Texture locations for portal effect
    private static final ResourceLocation PORTAL_TEXTURE = new ResourceLocation(MythandMetal.MOD_ID,
            "textures/block/dungeon_portal.png");

    // Render constants
    private static final float PULSE_SPEED = 0.05f;
    private static final float MAX_GLOW_INTENSITY = 1.5f;
    private static final float MIN_GLOW_INTENSITY = 0.8f;

    public DungeonPortalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ModDungeonPortalDoorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // Calculate pulsating glow intensity
        float time = Minecraft.getInstance().level.getGameTime() + partialTick;
        float glowIntensity = calculateGlowIntensity(time);

        // Render portal base
        Matrix4f matrix = poseStack.last().pose();
        renderPortalBase(blockEntity, matrix, buffer, glowIntensity);

        // Render magical particle swirls
        renderMagicalParticles(blockEntity, poseStack, buffer, time);
    }

    private float calculateGlowIntensity(float time) {
        // Create a smooth, pulsating glow effect
        return MIN_GLOW_INTENSITY + (MAX_GLOW_INTENSITY - MIN_GLOW_INTENSITY) *
                (0.5f + 0.5f * (float)Math.sin(time * PULSE_SPEED));
    }

    private void renderPortalBase(ModDungeonPortalDoorBlockEntity blockEntity, Matrix4f matrix,
                                  MultiBufferSource buffer, float glowIntensity) {
        VertexConsumer consumer = buffer.getBuffer(getRenderType());

        // Render portal faces with gradient glow
        renderFace(blockEntity, matrix, consumer, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                Direction.SOUTH, glowIntensity);
        renderFace(blockEntity, matrix, consumer, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                Direction.NORTH, glowIntensity);
        renderFace(blockEntity, matrix, consumer, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                Direction.EAST, glowIntensity);
        renderFace(blockEntity, matrix, consumer, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                Direction.WEST, glowIntensity);
        renderFace(blockEntity, matrix, consumer, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                Direction.DOWN, glowIntensity);
        renderFace(blockEntity, matrix, consumer, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                Direction.UP, glowIntensity);
    }

    private void renderFace(ModDungeonPortalDoorBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer,
                            float x0, float x1, float y0, float y1,
                            float z0, float z1, float z2, float z3,
                            Direction direction, float glowIntensity) {
        // Apply gradient glow based on distance from edge
        consumer.vertex(pose, x0, y0, z0)
                .color(1f, 1f, 1f, calculateEdgeGlow(x0, y0, glowIntensity))
                .endVertex();
        consumer.vertex(pose, x1, y0, z1)
                .color(1f, 1f, 1f, calculateEdgeGlow(x1, y0, glowIntensity))
                .endVertex();
        consumer.vertex(pose, x1, y1, z2)
                .color(1f, 1f, 1f, calculateEdgeGlow(x1, y1, glowIntensity))
                .endVertex();
        consumer.vertex(pose, x0, y1, z3)
                .color(1f, 1f, 1f, calculateEdgeGlow(x0, y1, glowIntensity))
                .endVertex();
    }

    private float calculateEdgeGlow(float x, float y, float baseIntensity) {
        // Create a radial glow effect that's more intense near edges
        float distanceFromCenter = (float)Math.sqrt(
                Math.pow(x - 0.5f, 2) + Math.pow(y - 0.5f, 2)
        );
        return baseIntensity * (1 - distanceFromCenter * 1.5f);
    }

    private void renderMagicalParticles(ModDungeonPortalDoorBlockEntity blockEntity, PoseStack poseStack,
                                        MultiBufferSource buffer, float time) {
        Vec3 portalCenter = Vec3.atCenterOf(blockEntity.getBlockPos());

        for (int i = 0; i < 10; i++) {
            float angle = time * 0.2f + i * (2 * (float)Math.PI / 10);
            float radius = 0.5f;

            // Calculate particle position in a circular swirl
            double particleX = portalCenter.x + radius * Math.cos(angle);
            double particleY = portalCenter.y + i * 0.1f;
            double particleZ = portalCenter.z + radius * Math.sin(angle);

            Minecraft.getInstance().level.addParticle(
                    ModParticleTypes.DUNGEON_PORTAL_PARTICLE.get(),
                    particleX, particleY, particleZ,
                    0.0, 0.1, 0.0
            );
        }
    }

    protected RenderType getRenderType() {
        return RenderType.endPortal();
    }



    @Override
    public int getViewDistance() {
        return 256;
    }
}