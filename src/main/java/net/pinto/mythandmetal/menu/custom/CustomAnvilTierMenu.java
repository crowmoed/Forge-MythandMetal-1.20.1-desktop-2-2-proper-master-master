package net.pinto.mythandmetal.menu.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.pinto.mythandmetal.block.custom.CustomAnvilTierBlock;
import net.pinto.mythandmetal.item.customfun.*;
import net.pinto.mythandmetal.rarity.ItemRarityHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

import static net.minecraftforge.common.ForgeHooks.onAnvilRepair;

public class CustomAnvilTierMenu extends ItemCombinerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final boolean DEBUG_COST = false;
    public static final int MAX_NAME_LENGTH = 50;
    public int repairItemCountCost;
    @Nullable
    private String itemName;
    private final DataSlot cost = DataSlot.standalone();
    private static final int COST_FAIL = 0;
    private static final int COST_BASE = 1;
    private static final int COST_ADDED_BASE = 1;
    private static final int COST_REPAIR_MATERIAL = 1;
    private static final int COST_REPAIR_SACRIFICE = 2;
    private static final int COST_INCOMPATIBLE_PENALTY = 1;
    private static final int COST_RENAME = 1;
    private static final int INPUT_SLOT_X_PLACEMENT = 27;
    private static final int ADDITIONAL_SLOT_X_PLACEMENT = 76;
    private static final int RESULT_SLOT_X_PLACEMENT = 134;
    private static final int SLOT_Y_PLACEMENT = 47;

    public CustomAnvilTierMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public CustomAnvilTierMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(MenuType.ANVIL, pContainerId, pPlayerInventory, pAccess);
        this.addDataSlot(this.cost);
    }

    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (p_266635_) -> {
            return true;
        }).withSlot(1, 76, 47, (p_266634_) -> {
            return true;
        }).withResultSlot(2, 134, 47).build();
    }

    protected boolean isValidBlock(BlockState pState) {
        return pState.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return (pPlayer.getAbilities().instabuild || pPlayer.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    protected void onTake(Player pPlayer, ItemStack pStack) {
        if (!pPlayer.getAbilities().instabuild) {
            pPlayer.giveExperienceLevels(-this.cost.get());
        }

        float breakChance = onAnvilRepair(pPlayer, pStack, this.inputSlots.getItem(0), this.inputSlots.getItem(1));

        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                itemstack.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost.set(0);
        this.access.execute((p_150479_, p_150480_) -> {
            BlockState blockstate = p_150479_.getBlockState(p_150480_);
            if (!pPlayer.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL) && pPlayer.getRandom().nextFloat() < breakChance) {
                BlockState blockstate1 = CustomAnvilTierBlock.damage(blockstate);
                if (blockstate1 == null) {
                    p_150479_.removeBlock(p_150480_, false);
                    p_150479_.levelEvent(1029, p_150480_, 0);
                } else {
                    p_150479_.setBlock(p_150480_, blockstate1, 2);
                    p_150479_.levelEvent(1030, p_150480_, 0);
                }
            } else {
                p_150479_.levelEvent(1030, p_150480_, 0);
            }

        });
    }


    public static boolean onAnvilChange(CustomAnvilTierMenu container, @NotNull ItemStack left, @NotNull ItemStack right, Container outputSlot, String name, int baseCost, Player player)
    {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost, player);
        if (MinecraftForge.EVENT_BUS.post(e)) return false;
        if (e.getOutput().isEmpty()) return true;

        outputSlot.setItem(0, e.getOutput());
        container.setMaximumCost(e.getCost());
        container.repairItemCountCost = e.getMaterialCost();
        return false;
    }

    public void createResult() {
        ItemStack itemstack = this.inputSlots.getItem(0);
        this.cost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemstack.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j += itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
            this.repairItemCountCost = 0;
            boolean flag = false;

            if (!onAnvilChange(this, itemstack, itemstack2, resultSlots, itemName, j, this.player)) return;
            if (!itemstack2.isEmpty()) {
                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(itemstack2).isEmpty();
                if (itemstack1.isDamageableItem() && itemstack1.getItem().isValidRepairItem(itemstack, itemstack2)) {
                    int l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    int i3;
                    for(i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
                        int j3 = itemstack1.getDamageValue() - l2;
                        itemstack1.setDamageValue(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    }

                    this.repairItemCountCost = i3;
                } else if (!(itemstack2.getItem() instanceof UpgradeIngot1 ||itemstack2.getItem() instanceof UpgradeIngot3||itemstack2.getItem() instanceof UpgradeIngot6 ||itemstack2.getItem() instanceof UpgradeIngot4||itemstack2.getItem() instanceof UpgradeIngot2||itemstack2.getItem() instanceof UpgradeIngot5)) {
                    if (!flag && (!itemstack1.is(itemstack2.getItem()) || !itemstack1.isDamageableItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    if (itemstack1.isDamageableItem() && !flag) {
                        int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamageValue()) {
                            itemstack1.setDamageValue(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for(Enchantment enchantment1 : map1.keySet()) {
                        if (enchantment1 != null) {
                            int i2 = map.getOrDefault(enchantment1, 0);
                            int j2 = map1.get(enchantment1);
                            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1.canEnchant(itemstack);
                            if (this.player.getAbilities().instabuild || itemstack.is(Items.ENCHANTED_BOOK)) {
                                flag1 = true;
                            }

                            for(Enchantment enchantment : map.keySet()) {
                                if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                                    flag1 = false;
                                    ++i;
                                }
                            }

                            if (!flag1) {
                                flag3 = true;
                            } else {
                                flag2 = true;
                                if (j2 > enchantment1.getMaxLevel()) {
                                    j2 = enchantment1.getMaxLevel();
                                }

                                map.put(enchantment1, j2);
                                int k3 = 0;
                                switch (enchantment1.getRarity()) {
                                    case COMMON:
                                        k3 = 1;
                                        break;
                                    case UNCOMMON:
                                        k3 = 2;
                                        break;
                                    case RARE:
                                        k3 = 4;
                                        break;
                                    case VERY_RARE:
                                        k3 = 8;
                                }

                                if (flag) {
                                    k3 = Math.max(1, k3 / 2);
                                }

                                i += k3 * j2;
                                if (itemstack.getCount() > 1) {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag3 && !flag2) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                }
            }

            if (this.itemName != null && !Util.isBlank(this.itemName)) {
                if (!this.itemName.equals(itemstack.getHoverName().getString())) {
                    k = 1;
                    i += k;
                    itemstack1.setHoverName(Component.literal(this.itemName));
                }
            } else if (itemstack.hasCustomHoverName()) {
                k = 1;
                i += k;
                itemstack1.resetHoverName();
            }
            if (flag && !itemstack1.isBookEnchantable(itemstack2)) itemstack1 = ItemStack.EMPTY;

            this.cost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.cost.get() >= 40) {
                this.cost.set(39);
            }

            if (this.cost.get() >= 40 && !this.player.getAbilities().instabuild) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getBaseRepairCost();
                if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
                    k2 = itemstack2.getBaseRepairCost();
                }

                if (k != i || k == 0) {
                    k2 = calculateIncreasedRepairCost(k2);
                }

                itemstack1.setRepairCost(k2);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }
            if((ItemRarityHelper.getRarity(itemstack).getMultiplier())<(ItemRarityHelper.getRarity(itemstack2).getMultiplier()))
                if(!itemstack1.isEmpty()){
                ItemRarityHelper.setRarity(itemstack1,ItemRarityHelper.getRarity(itemstack2));
                this.cost.set(this.cost.get()+2);}
                else {
                    itemstack1 = itemstack.copy();
                    ItemRarityHelper.setRarity(itemstack1,ItemRarityHelper.getRarity(itemstack2));
                    this.cost.set(2);

                }
            this.resultSlots.setItem(0, itemstack1);
            this.broadcastChanges();
        }
    }

    public static int calculateIncreasedRepairCost(int pOldRepairCost) {
        return pOldRepairCost * 2 + 1;
    }

    public boolean setItemName(String pItemName) {
        String s = validateName(pItemName);
        if (s != null && !s.equals(this.itemName)) {
            this.itemName = s;
            if (this.getSlot(2).hasItem()) {
                ItemStack itemstack = this.getSlot(2).getItem();
                if (Util.isBlank(s)) {
                    itemstack.resetHoverName();
                } else {
                    itemstack.setHoverName(Component.literal(s));
                }
            }

            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private static String validateName(String pItemName) {
        String s = SharedConstants.filterText(pItemName);
        return s.length() <= 50 ? s : null;
    }

    /**
     * Gets the maximum xp cost
     */
    public int getCost() {
        return this.cost.get();
    }

    public void setMaximumCost(int value) {
        this.cost.set(value);
    }







}