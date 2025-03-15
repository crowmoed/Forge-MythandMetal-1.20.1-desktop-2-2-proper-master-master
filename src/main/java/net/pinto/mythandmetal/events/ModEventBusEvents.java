package net.pinto.mythandmetal.events;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pinto.mythandmetal.MythandMetal;
import net.pinto.mythandmetal.entity.AshenWolf.AshenWolf;
import net.pinto.mythandmetal.entity.AshenWolf.AshenWolfModel;
import net.pinto.mythandmetal.entity.ModEntites;
import net.pinto.mythandmetal.entity.client.ModModelLayers;

@Mod.EventBusSubscriber(modid = MythandMetal.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntites.ASHENWOLF.get(), AshenWolf.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ASHENWOLF_LAYER, AshenWolfModel::createBodyLayer);


    }


}
