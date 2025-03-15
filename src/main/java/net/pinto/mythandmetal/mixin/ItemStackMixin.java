package net.pinto.mythandmetal.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.pinto.mythandmetal.item.customfun.*;
import net.pinto.mythandmetal.rarity.RarityLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static net.pinto.mythandmetal.rarity.RarityLevel.TIERONE;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
    private void addRarityTooltip(@Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        List<Component> tooltip = cir.getReturnValue();

        if (stack.hasTag() && stack.getTag().contains("Rarity") && (stack.getItem().isDamageable(stack) || stack.getItem() instanceof TieredItem || stack.getItem() instanceof UpgradeIngot1|| stack.getItem() instanceof UpgradeIngot2|| stack.getItem() instanceof UpgradeIngot3|| stack.getItem() instanceof UpgradeIngot5|| stack.getItem() instanceof UpgradeIngot6
                || stack.getItem() instanceof UpgradeIngot4)) {
            String rarity = stack.getTag().getString("Rarity");
            Component rarityText = Component.literal("Rarity: " + rarity)
                    .withStyle(ChatFormatting.GOLD); 

            tooltip.add(rarityText);
        }

        cir.setReturnValue(tooltip);
    }


    @Redirect(
            method = "getTooltipLines",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F")
    )
    private float redirectGetDamageBonus(ItemStack stack, MobType mobType) {
        return getCustomDamageBonus(stack, mobType);
    }


    private float getBaseDamage(ItemStack itemStack) {
        float baseDamage = 0.0F;
        Multimap<Attribute, AttributeModifier> modifiers = itemStack.getAttributeModifiers(EquipmentSlot.MAINHAND);

        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey().equals(Attributes.ATTACK_DAMAGE)) {
                baseDamage += entry.getValue().getAmount();
            }
        }

        return baseDamage;
    }
    private float getCustomDamageBonus(ItemStack itemStack, MobType mobType) {
        float baseDamage = getBaseDamage(itemStack);

        RarityLevel rarity = getCustomRarity(itemStack);

        float totalDamage = baseDamage * rarity.getMultiplier();

        float enchantmentBonus = EnchantmentHelper.getDamageBonus(itemStack, mobType);
        return totalDamage + enchantmentBonus-baseDamage;
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
}
