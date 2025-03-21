package net.pinto.mythandmetal.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.block.ModBlocks;
import net.pinto.mythandmetal.entity.CustVillager;
import net.pinto.mythandmetal.item.ModItems;

import java.util.List;

@Mod.EventBusSubscriber(modid = MythandMetal.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        if(event.getType() == CustVillager.MYTHRIL_TRADER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // Add all trades at level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 16),
                    new ItemStack(ModBlocks.TIER_ANVIL.get(), 1),
                    16, 8, 0.02f));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.UPGRADE_INGOT_1.get(), 1),
                    10, 8, 0.02f));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(ModItems.UPGRADE_INGOT_2.get(), 1),
                    5, 8, 0.035f));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.UPGRADE_INGOT_3.get(), 1),
                    2, 8, 0.075f));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.UPGRADE_INGOT_4.get(), 1),
                    2, 8, 0.075f));

            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.UPGRADE_INGOT_5.get(), 1),
                    2, 8, 0.075f));

            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.UPGRADE_INGOT_6.get(), 1),
                    2, 8, 0.075f));
        }

    }

}