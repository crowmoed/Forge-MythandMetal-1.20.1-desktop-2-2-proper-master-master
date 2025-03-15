package net.pinto.mythandmetal.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.pinto.mythandmetal.rarity.ItemRarityHelper;
import net.pinto.mythandmetal.rarity.RarityLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.pinto.mythandmetal.rarity.RarityLevel.TIERONE;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void assignRarityOnPickup(CallbackInfo ci) {
        Player player = (Player) (Object) this;

        for (ItemStack stack : player.getInventory().items) {
            if((stack.getItem() instanceof SwordItem || stack.getItem() instanceof ArmorItem))
            if (!stack.isEmpty() && !stack.getOrCreateTag().contains("Rarity")) {
                RarityLevel rarity = rollRandomRarity(stack);
                ItemRarityHelper.setRarity(stack, rarity);
            }
        }
        if (player.containerMenu instanceof ChestMenu chestMenu) {
            for (ItemStack stack : chestMenu.getItems()) {
                if (stack.isEmpty() || stack.getOrCreateTag().contains("Rarity")) {
                    continue;  
                }

                RarityLevel rarity = rollRandomRarity(stack);
                ItemRarityHelper.setRarity(stack, rarity);  
            }
        }
    }

    private RarityLevel rollRandomRarity(ItemStack itemStack) {
        double random = Math.random();
        if((itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof ArmorItem))
        {
        if ((random < 0.5)) {
            return RarityLevel.TIERONE;
        } else if (random < 0.75) {
            return rollSubRarity(RarityLevel.TIERTWO, RarityLevel.TIERTWO);
        } else if (random < 0.9) {
            return rollSubRarity(RarityLevel.TIERTHREA, RarityLevel.TIERTHREA);
        } else if (random < 0.98) {
            return rollSubRarity(RarityLevel.TIERFOUR, RarityLevel.TIERFOUR);
        } else {
            return rollSubRarity(RarityLevel.TIERFIVE, RarityLevel.TIERFIVE);
        }
        }
        return TIERONE;
    }

    private RarityLevel rollSubRarity(RarityLevel minLevel, RarityLevel maxLevel) {
        int range = maxLevel.ordinal() - minLevel.ordinal() + 1;
        int randomIndex = (int) (Math.random() * range) + minLevel.ordinal();
        return RarityLevel.values()[randomIndex];
    }







    @Redirect(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
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
