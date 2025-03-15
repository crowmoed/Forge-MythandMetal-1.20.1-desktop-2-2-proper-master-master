package net.pinto.mythandmetal.item.customfun;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.pinto.mythandmetal.rarity.ItemRarityHelper;
import net.pinto.mythandmetal.rarity.RarityLevel;

public class UpgradeIngotScrap6 extends Item {


    public UpgradeIngotScrap6(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public boolean isBarVisible(ItemStack pStack) {
        ensureNBT(pStack);
        return pStack.isDamaged();
    }

    private void ensureNBT(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ItemRarityHelper.setRarity(stack, RarityLevel.TIERSEVEN);
    }


    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64; // This makes the item non-stackable
    }



}
