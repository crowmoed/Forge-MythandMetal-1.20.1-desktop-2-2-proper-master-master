package net.pinto.mythandmetal.item.customfun;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.pinto.mythandmetal.rarity.ItemRarityHelper;
import net.pinto.mythandmetal.rarity.RarityLevel;

public class UpgradeIngot5 extends Item {


    public UpgradeIngot5(Properties pProperties) {

        super(pProperties);
    }
    @Override
    public boolean isBarVisible(ItemStack pStack) {
        ensureNBT(pStack);
        return pStack.isDamaged();
    }



    private void ensureNBT(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ItemRarityHelper.setRarity(stack, RarityLevel.TIERSIX);
    }





    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1; // This makes the item non-stackable
    }
}
