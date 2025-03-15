package net.pinto.mythandmetal.mixin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.Cancelable;
import net.pinto.mythandmetal.rarity.ItemRarityHelper;
import net.pinto.mythandmetal.rarity.RarityLevel;
import net.pinto.mythandmetal.renderer.GlintRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Map;

import static net.pinto.mythandmetal.rarity.RarityLevel.TIERONE;


@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorRendererMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    @Shadow
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    @Shadow
    private final A innerModel;
    @Shadow
    private final A outerModel;
    @Shadow
    private final TextureAtlas armorTrimAtlas;

    public ArmorRendererMixin(RenderLayerParent<T, M> pRenderer, A pInnerModel, A pOuterModel, ModelManager pModelManager) {
        super(pRenderer);
        this.innerModel = pInnerModel;
        this.outerModel = pOuterModel;
        this.armorTrimAtlas = pModelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    @Inject(method ="render",at = @At(value = "HEAD"),cancellable = true)
    private void replaceRenderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        this.renderArmorPieceOutline(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.CHEST, pPackedLight, this.cusgetArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPieceOutline(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.LEGS, pPackedLight, this.cusgetArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPieceOutline(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.FEET, pPackedLight, this.cusgetArmorModel(EquipmentSlot.FEET));
        this.renderArmorPieceOutline(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.HEAD, pPackedLight, this.cusgetArmorModel(EquipmentSlot.HEAD));

        ci.cancel();

    }

    private void renderArmorPieceOutline(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(pSlot);
        Item $$9 = itemstack.getItem();

        if ($$9 instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == pSlot) {
                this.getParentModel().copyPropertiesTo(pModel);
                this.cussetPartVisibility(pModel, pSlot);
                net.minecraft.client.model.Model model = cusgetArmorModelHook(pLivingEntity, itemstack, pSlot, pModel);
                boolean flag = this.usesInnerModel(pSlot);
                if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
                    int i = ((net.minecraft.world.item.DyeableLeatherItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    this.cusrenderModel(pPoseStack, pBuffer, pPackedLight, armoritem, model, flag, f, f1, f2, this.cusgetArmorResource(pLivingEntity, itemstack, pSlot, null));
                    this.cusrenderModel(pPoseStack, pBuffer, pPackedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, this.cusgetArmorResource(pLivingEntity, itemstack, pSlot, "overlay"));
                } else {
                    this.cusrenderModel(pPoseStack, pBuffer, pPackedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, this.cusgetArmorResource(pLivingEntity, itemstack, pSlot, null));
                }

                ArmorTrim.getTrim(pLivingEntity.level().registryAccess(), itemstack).ifPresent((p_289638_) -> {
                    this.cusrenderTrim(armoritem.getMaterial(), pPoseStack, pBuffer, pPackedLight, p_289638_, model, flag);
                });
                if (ItemRarityHelper.getRarity(itemstack).getMultiplier()>1) {
                    //this.getEnchantmentLevel(getTotalEnchantmentLevels(itemstack),pPoseStack, pBuffer, pPackedLight, model);
                    getRarityCase(ItemRarityHelper.getRarity(itemstack),pPoseStack,pBuffer,pPackedLight,model,true);
                }
                if (itemstack.hasFoil()) {
                    this.getEnchantmentLevel(getTotalEnchantmentLevels(itemstack),pPoseStack, pBuffer, pPackedLight, model);
                    //getRarityCase(ItemRarityHelper.getRarity(itemstack),pPoseStack,pBuffer,pPackedLight,model,true);
                }
            }
        }
    }

    public int getTotalEnchantmentLevels(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        return enchantments.values().stream().mapToInt(Integer::intValue).sum();
    }

    protected void cussetPartVisibility(A pModel, EquipmentSlot pSlot) {
        pModel.setAllVisible(false);
        switch (pSlot) {
            case HEAD:
                pModel.head.visible = true;
                pModel.hat.visible = true;
                break;
            case CHEST:
                pModel.body.visible = true;
                pModel.rightArm.visible = true;
                pModel.leftArm.visible = true;
                break;
            case LEGS:
                pModel.body.visible = true;
                pModel.rightLeg.visible = true;
                pModel.leftLeg.visible = true;
                break;
            case FEET:
                pModel.rightLeg.visible = true;
                pModel.leftLeg.visible = true;
        }

    }
    private void getRarityCase(RarityLevel rarity,PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel,Boolean isenchanted) {
        switch (rarity) {
            case TIERTWO: this.renderTierTwoGlint(pPoseStack, pBuffer, pPackedLight, pModel);break;

            case TIERTHREA: this.renderTierThreaGlint(pPoseStack, pBuffer, pPackedLight, pModel);break;

            case TIERFOUR: this.renderTierFourGlint(pPoseStack, pBuffer, pPackedLight, pModel);break;

            case TIERFIVE: this.renderTierFiveGlint(pPoseStack, pBuffer, pPackedLight, pModel);break;

            case TIERSIX: this.renderTierSixGlint(pPoseStack, pBuffer, pPackedLight, pModel);break;

            case TIERSEVEN: this.renderTierSevenGlint(pPoseStack, pBuffer, pPackedLight, pModel);break;
        }
    }




    private void renderTierFiveGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getTierFivearmorEntityGlintOutline()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void renderTierFourGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getTierFourarmorEntityGlintOutline()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderTierThreaGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getTierThreaArmorEntityGlintOutline()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderTierTwoGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getTierTwoArmorEntityGlintOutline()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderTierSixGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getTierSixArmorEntityGlintOutline()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void renderTierSevenGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getTierSevenArmorEntityGlintOutline()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected net.minecraft.client.model.Model cusgetArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }

    private void cusrenderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, A pModel) {
        cusrenderGlint(pPoseStack, pBuffer, pPackedLight, pModel);
    }
    private void cusrenderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.armorEntityGlint()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void cusrenderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorItem pArmorItem, A pModel, boolean pWithGlint, float pRed, float pGreen, float pBlue, @Nullable String pArmorSuffix) {
        cusrenderModel(pPoseStack, pBuffer, pPackedLight, pArmorItem, pModel, pWithGlint, pRed, pGreen, pBlue, this.getArmorLocation(pArmorItem, pWithGlint, pArmorSuffix));
    }
    private void cusrenderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorItem pArmorItem, net.minecraft.client.model.Model pModel, boolean pWithGlint, float pRed, float pGreen, float pBlue, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));

        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, pRed, pGreen, pBlue, 1.0F);
    }

    private void cusrenderTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorTrim pTrim, A pModel, boolean pInnerTexture) {
        cusrenderTrim(pArmorMaterial, pPoseStack, pBuffer, pPackedLight, pTrim, pModel, pInnerTexture);
    }
    private void cusrenderTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorTrim pTrim, net.minecraft.client.model.Model pModel, boolean pInnerTexture) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(pInnerTexture ? pTrim.innerTexture(pArmorMaterial) : pTrim.outerTexture(pArmorMaterial));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(pBuffer.getBuffer(Sheets.armorTrimsSheet()));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }




    private A cusgetArmorModel(EquipmentSlot pSlot) {
        return (A)(this.usesInnerModel(pSlot) ? this.innerModel : this.outerModel);
    }

    private boolean usesInnerModel(EquipmentSlot pSlot) {
        return pSlot == EquipmentSlot.LEGS;
    }

    @Deprecated //Use the more sensitive version getArmorResource below
    private ResourceLocation getArmorLocation(ArmorItem pArmorItem, boolean pLayer2, @Nullable String pSuffix) {
        String s = "textures/models/armor/" + pArmorItem.getMaterial().getName() + "_layer_" + (pLayer2 ? 2 : 1) + (pSuffix == null ? "" : "_" + pSuffix) + ".png";
        return ARMOR_LOCATION_CACHE.computeIfAbsent(s, ResourceLocation::new);
    }

    /*=================================== FORGE START =========================================*/




    public ResourceLocation cusgetArmorResource(net.minecraft.world.entity.Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem)stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (usesInnerModel(slot) ? 2 : 1), type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }

        return resourcelocation;
    }
    private void getEnchantmentLevel(int enchantment, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Model pModel) {


        if(enchantment<6)  {getFoilBufferDirectEnchantment1(pPoseStack, pBuffer, pPackedLight, pModel);}
        else if(enchantment<12) {getFoilBufferDirectEnchantment2(pPoseStack, pBuffer, pPackedLight, pModel);}
        else if(enchantment<19) {getFoilBufferDirectEnchantment3(pPoseStack, pBuffer, pPackedLight, pModel);}
        else getFoilBufferDirectEnchantment4(pPoseStack, pBuffer, pPackedLight, pModel);

    }
    private void getFoilBufferDirectEnchantment1(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getarmentity_glint_direct1()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void getFoilBufferDirectEnchantment2(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getarmentity_glint_direct2()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void getFoilBufferDirectEnchantment3(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getarmentity_glint_direct3()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void getFoilBufferDirectEnchantment4(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(GlintRenderers.getarmentity_glint_direct4()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
