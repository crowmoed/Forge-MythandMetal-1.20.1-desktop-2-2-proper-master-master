package net.pinto.mythandmetal.mixin;


import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.pinto.mythandmetal.item.customfun.*;
import net.pinto.mythandmetal.rarity.ItemRarityHelper;
import net.pinto.mythandmetal.rarity.RarityLevel;
import net.pinto.mythandmetal.renderer.GlintRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.minecraft.client.renderer.entity.ItemRenderer.*;
import static net.pinto.mythandmetal.rarity.RarityLevel.TIERONE;



@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ResourceManagerReloadListener {

    @Shadow
    private ItemColors itemColors;


    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getFoilBufferDirect(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
    )
    public VertexConsumer injectBeforeBuffer1(
            MultiBufferSource multiBuffer,
            RenderType renderType,
            boolean isFoil,
            boolean isGlint,
            ItemStack item,
            ItemDisplayContext context,
            boolean bool
    ) {

        LocalPlayer localplayer = Minecraft.getInstance().player;

        if (localplayer != null && item.getTag() != null) {
            if (item.getTag().contains("Rarity") &&
                    (item.getItem().isDamageable(item) ||
                            item.getItem() instanceof TieredItem ||
                            item.getItem() instanceof UpgradeIngot1 ||
                            item.getItem() instanceof UpgradeIngot2 ||
                            item.getItem() instanceof UpgradeIngot3 ||
                            item.getItem() instanceof UpgradeIngot4)) {




                //return getRarityCaseDirect2(getCustomRarity(item), multiBuffer, renderType, true, item.hasFoil());
            }
        }
        return getEnchantmentLevel(getTotalEnchantmentLevels(item), multiBuffer, renderType, true, item.hasFoil(),item);
    }
    /*@Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void renderOutlineBehind(
            ItemStack stack,
            ItemDisplayContext context,
            boolean leftHand,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            int combinedOverlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        if (shouldRenderOutline(stack)) {
            // Save OpenGL state
            RenderSystem.backupProjectionMatrix();
            poseStack.pushPose();

            // Configure depth offset to force outline behind
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.polygonOffset(-1.0F, -1.0F); // Offset to render "behind"
            RenderSystem.enablePolygonOffset();

            // Slightly scale up the outline
            poseStack.scale(1.03F, 1.03F, 1.03F);

            // Render wireframe outline
            VertexConsumer outlineBuffer = getRarityCaseDirect2(getCustomRarity(stack), buffer, RenderType.glint(), true, stack.hasFoil());

            this.cusrenderModelLists(
                    model,
                    stack,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    outlineBuffer
            );

            // Restore OpenGL state
            RenderSystem.disablePolygonOffset();
            poseStack.popPose();
            RenderSystem.restoreProjectionMatrix();
        }
    }*/


    // Example condition for items that should have an outline
    private boolean shouldRenderOutline(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Rarity");
    }
    public int getTotalEnchantmentLevels(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        return enchantments.values().stream().mapToInt(Integer::intValue).sum();
    }
    @Inject(method = "render", at = @At("HEAD"))
    public  void renderOutline2(ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel,CallbackInfo ci) {

        if (ItemRarityHelper.getRarity(pItemStack).getMultiplier()>1.0&&pItemStack.getItem()instanceof TieredItem||
                pItemStack.getItem() instanceof UpgradeIngot1 ||
                pItemStack.getItem() instanceof UpgradeIngot2 ||
                pItemStack.getItem() instanceof UpgradeIngot3 ||
                pItemStack.getItem() instanceof UpgradeIngot4 ||
                pItemStack.getItem() instanceof UpgradeIngot5 ||
                pItemStack.getItem() instanceof UpgradeIngot6||
                pItemStack.getItem() instanceof ArmorItem ){

            if (!isModel3D(pModel, pItemStack)) {

                renderOutline3(pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay, pModel);
                renderOutline(pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay, pModel);
            }
            else{                renderOutline3(pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay, pModel);
            }
             }
    }

    private boolean isModel3D(BakedModel model, ItemStack stack) {
        RandomSource random = RandomSource.create();
        int quadCount = 0;

        // Count quads for all directions
        for (Direction direction : Direction.values()) {
            quadCount += model.getQuads(null, direction, random).size();
        }

        // Count quads for the general case (no specific direction)
        quadCount += model.getQuads(null, null, random).size();

        // If the model has quads for multiple directions, it's likely 3D
        return quadCount < 50; // Adjust this threshold as needed
    }
    private void renderOutline(ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel) {

        if (!pItemStack.isEmpty()) {
            pPoseStack.pushPose();
            boolean flag = pDisplayContext == ItemDisplayContext.GUI || pDisplayContext == ItemDisplayContext.GROUND || pDisplayContext == ItemDisplayContext.FIXED;
            if (flag) {
                if (pItemStack.is(Items.TRIDENT)) {
                    pModel = this.itemModelShaper.getModelManager().getModel(TRIDENT_MODEL);
                } else if (pItemStack.is(Items.SPYGLASS)) {
                    pModel = this.itemModelShaper.getModelManager().getModel(SPYGLASS_MODEL);
                }
            }


            pModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(pPoseStack, pModel, pDisplayContext, pLeftHand);


            pPoseStack.translate(-0.5065, -0.5065, 1.015F/2);
            pPoseStack.scale(1.015F, 1.015F, -1.015F);


            if (!pModel.isCustomRenderer() && (!pItemStack.is(Items.TRIDENT) || flag)) {
                boolean flag1;
                if (pDisplayContext != ItemDisplayContext.GUI && !pDisplayContext.firstPerson() && pItemStack.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)pItemStack.getItem()).getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    flag1 = true;
                }
                for (var model : pModel.getRenderPasses(pItemStack, flag1)) {
                    for (var rendertype : model.getRenderTypes(pItemStack, flag1)) {
                        VertexConsumer vertexconsumer;
                        if (cushasAnimatedTexture(pItemStack) && pItemStack.hasFoil()) {
                            pPoseStack.pushPose();
                            PoseStack.Pose posestack$pose = pPoseStack.last();
                            if (pDisplayContext == ItemDisplayContext.GUI) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
                            } else if (pDisplayContext.firstPerson()) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
                            }

                            if (flag1) {
                                vertexconsumer = getCompassFoilBufferDirect(pBuffer, rendertype, posestack$pose);
                            } else {
                                vertexconsumer = getCompassFoilBuffer(pBuffer, rendertype, posestack$pose);
                            }

                            pPoseStack.popPose();
                        } else if (flag1) {
                            vertexconsumer = getRarityCaseDirect2(ItemRarityHelper.getRarity(pItemStack),pBuffer, rendertype, true, pItemStack.hasFoil());
                        } else {
                            vertexconsumer = getRarityCaseDirect2(ItemRarityHelper.getRarity(pItemStack),pBuffer, rendertype, true, pItemStack.hasFoil());
                        }
                        vertexconsumer = getRarityCaseDirect2(ItemRarityHelper.getRarity(pItemStack),pBuffer, rendertype, true, pItemStack.hasFoil());

                        this.cusrenderModelLists(model, pItemStack, 0, pCombinedOverlay, pPoseStack, vertexconsumer);
                    }
                }
            } else {
                net.minecraftforge.client.extensions.common.IClientItemExtensions.of(pItemStack).getCustomRenderer().renderByItem(pItemStack, pDisplayContext, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay);
            }

            pPoseStack.popPose();
        }

    }



    private void renderOutline3(ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel) {

        if (!pItemStack.isEmpty()) {
            pPoseStack.pushPose();
            boolean flag = pDisplayContext == ItemDisplayContext.GUI || pDisplayContext == ItemDisplayContext.GROUND || pDisplayContext == ItemDisplayContext.FIXED;
            if (flag) {
                if (pItemStack.is(Items.TRIDENT)) {
                    pModel = this.itemModelShaper.getModelManager().getModel(TRIDENT_MODEL);
                } else if (pItemStack.is(Items.SPYGLASS)) {
                    pModel = this.itemModelShaper.getModelManager().getModel(SPYGLASS_MODEL);
                }
            }


            pModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(pPoseStack, pModel, pDisplayContext, pLeftHand);


            pPoseStack.translate(-0.5, -0.5, -0.5);
            pPoseStack.scale(1.0F, 1.0F, 1.0F);


            if (!pModel.isCustomRenderer() && (!pItemStack.is(Items.TRIDENT) || flag)) {
                boolean flag1;
                if (pDisplayContext != ItemDisplayContext.GUI && !pDisplayContext.firstPerson() && pItemStack.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)pItemStack.getItem()).getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    flag1 = true;
                }
                for (var model : pModel.getRenderPasses(pItemStack, flag1)) {
                    for (var rendertype : model.getRenderTypes(pItemStack, flag1)) {
                        VertexConsumer vertexconsumer;
                        if (cushasAnimatedTexture(pItemStack) && pItemStack.hasFoil()) {
                            pPoseStack.pushPose();
                            PoseStack.Pose posestack$pose = pPoseStack.last();
                            if (pDisplayContext == ItemDisplayContext.GUI) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
                            } else if (pDisplayContext.firstPerson()) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
                            }

                            if (flag1) {
                                vertexconsumer = getCompassFoilBufferDirect(pBuffer, rendertype, posestack$pose);
                            } else {
                                vertexconsumer = getCompassFoilBuffer(pBuffer, rendertype, posestack$pose);
                            }

                            pPoseStack.popPose();
                        } else if (flag1) {
                            vertexconsumer = getRarityCaseDirect2(ItemRarityHelper.getRarity(pItemStack),pBuffer, rendertype, true, pItemStack.hasFoil());
                        } else {
                            vertexconsumer = getRarityCaseDirect2(ItemRarityHelper.getRarity(pItemStack),pBuffer, rendertype, true, pItemStack.hasFoil());
                        }
                        vertexconsumer = getRarityCase(ItemRarityHelper.getRarity(pItemStack),pBuffer, rendertype, true, pItemStack.hasFoil());

                        this.cusrenderModelLists(model, pItemStack, 0, pCombinedOverlay, pPoseStack, vertexconsumer);
                    }
                }
            } else {
                net.minecraftforge.client.extensions.common.IClientItemExtensions.of(pItemStack).getCustomRenderer().renderByItem(pItemStack, pDisplayContext, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay);
            }

            pPoseStack.popPose();
        }

    }
    private static final ResourceLocation ENCHANTED_GLINT_ENTITY = new ResourceLocation("textures/misc/enchanted_glint_entity.png");
    private static final ResourceLocation ENCHANTED_GLINT_ITEM = new ResourceLocation("textures/misc/enchanted_glint_item.png");
    private static final Set<Item> IGNORED = Sets.newHashSet(Items.AIR);
    private static final int GUI_SLOT_CENTER_X = 8;
    private static final int GUI_SLOT_CENTER_Y = 8;
    private static final int ITEM_COUNT_BLIT_OFFSET = 200;
    private static final float COMPASS_FOIL_UI_SCALE = 0.5F;
    private static final float COMPASS_FOIL_FIRST_PERSON_SCALE = 0.75F;
    private static final float COMPASS_FOIL_TEXTURE_SCALE = 0.0078125F;
    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    private static final ModelResourceLocation TRIDENT_IN_HAND_MODEL = ModelResourceLocation.vanilla("trident_in_hand", "inventory");
    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");
    private static final ModelResourceLocation SPYGLASS_IN_HAND_MODEL = ModelResourceLocation.vanilla("spyglass_in_hand", "inventory");
    private final Minecraft minecraft;
    private final TextureManager textureManager;
    private final BlockEntityWithoutLevelRenderer blockEntityRenderer;

    @Shadow
    private ItemModelShaper itemModelShaper;

    private static VertexConsumer cusgetFoilBuffer(MultiBufferSource pBuffer, RenderType pRenderType, boolean pIsItem, boolean pGlint) {
        if (pGlint) {
            return Minecraft.useShaderTransparency() && pRenderType == Sheets.translucentItemSheet() ? VertexMultiConsumer.create(pBuffer.getBuffer(RenderType.glintTranslucent()), pBuffer.getBuffer(pRenderType)) : VertexMultiConsumer.create(pBuffer.getBuffer(pIsItem ? RenderType.glint() : RenderType.entityGlint()), pBuffer.getBuffer(pRenderType));
        } else {
            return pBuffer.getBuffer(pRenderType);
        }
    }

    private static VertexConsumer cusgetFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? RenderType.glintDirect() : RenderType.entityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }




    private void cusrenderQuadList(PoseStack pPoseStack, VertexConsumer pBuffer, List<BakedQuad> pQuads, ItemStack pItemStack, int pCombinedLight, int pCombinedOverlay) {
        boolean flag = !pItemStack.isEmpty();
        PoseStack.Pose posestack$pose = pPoseStack.last();

        for(BakedQuad bakedquad : pQuads) {
            int i = -1;
            if (flag && bakedquad.isTinted()) {
                i = this.itemColors.getColor(pItemStack, bakedquad.getTintIndex());
            }

            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            pBuffer.putBulkData(posestack$pose, bakedquad, f, f1, f2, 1.0F, pCombinedLight, pCombinedOverlay, true);
        }

    }

    private BakedModel cusgetModel(ItemStack pStack, @Nullable Level pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        BakedModel bakedmodel;
        if (pStack.is(Items.TRIDENT)) {
            bakedmodel = this.itemModelShaper.getModelManager().getModel(TRIDENT_IN_HAND_MODEL);
        } else if (pStack.is(Items.SPYGLASS)) {
            bakedmodel = this.itemModelShaper.getModelManager().getModel(SPYGLASS_IN_HAND_MODEL);
        } else {
            bakedmodel = this.itemModelShaper.getItemModel(pStack);
        }

        ClientLevel clientlevel = pLevel instanceof ClientLevel ? (ClientLevel)pLevel : null;
        BakedModel bakedmodel1 = bakedmodel.getOverrides().resolve(bakedmodel, pStack, clientlevel, pEntity, pSeed);
        return bakedmodel1 == null ? this.itemModelShaper.getModelManager().getMissingModel() : bakedmodel1;
    }

    /*private void cusrenderStatic(ItemStack pStack, ItemDisplayContext pDisplayContext, int pCombinedLight, int pCombinedOverlay, PoseStack pPoseStack, MultiBufferSource pBuffer, @Nullable Level pLevel, int pSeed) {
        this.cusrenderStatic((LivingEntity)null, pStack, pDisplayContext, false, pPoseStack, pBuffer, pLevel, pCombinedLight, pCombinedOverlay, pSeed);
    }

    private void cusrenderStatic(@Nullable LivingEntity pEntity, ItemStack pItemStack, ItemDisplayContext pDiplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, @Nullable Level pLevel, int pCombinedLight, int pCombinedOverlay, int pSeed) {
        if (!pItemStack.isEmpty()) {
            BakedModel bakedmodel = this.cusgetModel(pItemStack, pLevel, pEntity, pSeed);
            this.renderOutline(pItemStack, pDiplayContext, pLeftHand, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay, bakedmodel);
        }
    }*/


    private BlockEntityWithoutLevelRenderer cusgetBlockEntityRenderer() {
        return blockEntityRenderer;
    }
    private ItemRendererMixin(Minecraft pMinecraft, TextureManager pTextureManager, ModelManager pModelManager, ItemColors pItemColors, BlockEntityWithoutLevelRenderer pBlockEntityRenderer) {
        this.minecraft = pMinecraft;
        this.textureManager = pTextureManager;
        this.itemModelShaper = new net.minecraftforge.client.model.ForgeItemModelShaper(pModelManager);
        this.blockEntityRenderer = pBlockEntityRenderer;

        for(Item item : BuiltInRegistries.ITEM) {
            if (!IGNORED.contains(item)) {
                this.itemModelShaper.register(item, new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(item), "inventory"));
            }
        }

        this.itemColors = pItemColors;

    }

    private ItemModelShaper cusgetItemModelShaper() {
        return this.itemModelShaper;
    }

    private void cusrenderModelLists(BakedModel pModel, ItemStack pStack, int pCombinedLight, int pCombinedOverlay, PoseStack pPoseStack, VertexConsumer pBuffer) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;

        for(Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            this.cusrenderQuadList(pPoseStack, pBuffer, pModel.getQuads((BlockState)null, direction, randomsource), pStack, pCombinedLight, pCombinedOverlay);
        }

        randomsource.setSeed(42L);
        this.cusrenderQuadList(pPoseStack, pBuffer, pModel.getQuads((BlockState)null, (Direction)null, randomsource), pStack, pCombinedLight, pCombinedOverlay);
    }
    private static boolean cushasAnimatedTexture(ItemStack pStack) {
        return pStack.is(ItemTags.COMPASSES) || pStack.is(Items.CLOCK);
    }

    private VertexConsumer getEnchantmentLevel(int enchantment,MultiBufferSource pBuffer, RenderType pRenderType, boolean pIsItem, boolean pGlint,ItemStack itemStack) {

            if(!(itemStack.getItem() instanceof TieredItem) &&!(itemStack.getItem() instanceof ArmorItem))

                return cusgetFoilBufferDirect(pBuffer, pRenderType, true, pGlint);
            if(enchantment<6) return getFoilBufferDirectEnchantment1(pBuffer, pRenderType, true, pGlint);
            if(enchantment<12)return getFoilBufferDirectEnchantment2(pBuffer, pRenderType, true, pGlint);
            if(enchantment<19)return getFoilBufferDirectEnchantment3(pBuffer, pRenderType, true, pGlint);
            return getFoilBufferDirectEnchantment4(pBuffer, pRenderType, true, pGlint);

        }


   private VertexConsumer getRarityCaseDirect2(RarityLevel rarity,MultiBufferSource pBuffer, RenderType pRenderType, boolean pIsItem, boolean pGlint) {
        switch (rarity) {
            case TIERONE:return getFoilBufferDirect(pBuffer, pRenderType, true, pGlint);

            case TIERTWO:return getTierTwoFoilBufferDirect(pBuffer, pRenderType, true, true)
                    ;

            case TIERTHREA:return getTierThreaFoilBufferDirect(pBuffer, pRenderType, true, true)
                    ;

            case TIERFOUR: return getTierFourFoilBufferDirect(pBuffer, pRenderType, true, true)
                    ;

            case TIERFIVE:return getTierFiveFoilBufferDirect(pBuffer, pRenderType, true, true)
                    ;
            case TIERSIX:return getTierSixFoilBufferDirect(pBuffer, pRenderType, true, true)
                    ;
            case TIERSEVEN:return getTierSevenFoilBufferDirect(pBuffer, pRenderType, true, true)
                    ;
            default: return  getFoilBufferDirect(pBuffer, pRenderType, true, pGlint)
                    ;
        }
    }

    private VertexConsumer getRarityCase(RarityLevel rarity,MultiBufferSource pBuffer, RenderType pRenderType, boolean pIsItem, boolean pGlint) {
        switch (rarity) {
            case TIERONE:return getFoilBufferDirect(pBuffer, pRenderType, true, pGlint);

            case TIERTWO:return getTierTwoFoilBufferDirectOutline(pBuffer, pRenderType, true, true)
                    ;

            case TIERTHREA:return getTierThreaFoilBufferDirectOutline(pBuffer, pRenderType, true, true)
                    ;

            case TIERFOUR: return getTierFourFoilBufferDirectOutline(pBuffer, pRenderType, true, true)
                    ;

            case TIERFIVE:return getTierFiveFoilBufferDirectOutline(pBuffer, pRenderType, true, true)
                    ;
            case TIERSIX:return getTierSixFoilBufferDirectOutline(pBuffer, pRenderType, true, true)
                    ;
            case TIERSEVEN:return getTierSevenFoilBufferDirectOutline(pBuffer, pRenderType, true, true)
                    ;
            default: return  getFoilBufferDirect(pBuffer, pRenderType, true, pGlint)
                    ;
        }
    }

    private RarityLevel getCustomRarity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        String rarityString = tag.getString("Rarity");
        try {
            return RarityLevel.valueOf(rarityString);
        } catch (IllegalArgumentException e) {
            return TIERONE;
        }
    }


    private static VertexConsumer getTierSevenFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierSevenGlintDirect() : GlintRenderers.getTierSevenEntityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getTierSixFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierSixGlintDirect() : GlintRenderers.getTierSixEntityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }

    private static VertexConsumer getTierFiveFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierFiveGlintDirect() : GlintRenderers.getTierFiveEntityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getTierFourFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierFourGlintDirect() : GlintRenderers.getTierFourEntityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }

    private static VertexConsumer getTierThreaFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierThreaGlintDirect() : GlintRenderers.getTierThreaEntityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }

    private static VertexConsumer getTierTwoFoilBufferDirect(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierTwoGlintDirect() : GlintRenderers.getTierTwoEntityGlintDirect()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }


    private static VertexConsumer getTierSevenFoilBufferDirectOutline(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierSevenGlintDirectOutline() : GlintRenderers.getTierSevenEntityGlintDirectOutline()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getTierSixFoilBufferDirectOutline(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierSixGlintDirectOutline() : GlintRenderers.getTierSixEntityGlintDirectOutline()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }

    private static VertexConsumer getTierFiveFoilBufferDirectOutline(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierFiveGlintDirectOutline() : GlintRenderers.getTierFiveEntityGlintDirectOutline()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getTierFourFoilBufferDirectOutline(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierFourGlintDirectOutline() : GlintRenderers.getTierFourEntityGlintDirectOutline()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }

    private static VertexConsumer getTierThreaFoilBufferDirectOutline(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierThreaGlintDirectOutline() : GlintRenderers.getTierThreaEntityGlintDirectOutline()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }

    private static VertexConsumer getTierTwoFoilBufferDirectOutline(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getTierTwoGlintDirectOutline() : GlintRenderers.getTierTwoEntityGlintDirectOutline()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }


    private static VertexConsumer getFoilBufferDirectEnchantment1(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getglint_direct1() : GlintRenderers.getentity_glint_direct1()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getFoilBufferDirectEnchantment2(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getglint_direct2() : GlintRenderers.getentity_glint_direct2()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getFoilBufferDirectEnchantment3(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getglint_direct3() : GlintRenderers.getentity_glint_direct3()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
    private static VertexConsumer getFoilBufferDirectEnchantment4(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint) {
        return pWithGlint ? VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? GlintRenderers.getglint_direct4() : GlintRenderers.getentity_glint_direct4()), pBuffer.getBuffer(pRenderType)) : pBuffer.getBuffer(pRenderType);
    }
}
