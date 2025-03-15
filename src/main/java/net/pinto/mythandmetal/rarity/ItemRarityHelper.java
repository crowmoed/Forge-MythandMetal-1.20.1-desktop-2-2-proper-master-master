package net.pinto.mythandmetal.rarity;

import net.minecraft.world.item.ItemStack;

public class ItemRarityHelper {
    private static final String RARITY_KEY = "Rarity";

    public static void setRarity(ItemStack stack, RarityLevel rarity) {
        stack.getOrCreateTag().putString(RARITY_KEY, rarity.name());
    }

    public static RarityLevel getRarity(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RARITY_KEY)) {
            return RarityLevel.valueOf(stack.getTag().getString(RARITY_KEY));
        }
        return RarityLevel.TIERONE;
    }


}