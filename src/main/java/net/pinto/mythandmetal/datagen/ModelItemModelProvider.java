package net.pinto.mythandmetal.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.MythandMetal;

public class ModelItemModelProvider extends ItemModelProvider {
    public ModelItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper){
        super(output, MythandMetal.MOD_ID,existingFileHelper);
    }

    @Override
    protected void registerModels() {




    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MythandMetal.MOD_ID,"block/" + item.getId().getPath()));
    }
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(MythandMetal.MOD_ID,"item/" + item.getId().getPath()));
    }
}
