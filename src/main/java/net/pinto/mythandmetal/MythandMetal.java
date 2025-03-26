package net.pinto.mythandmetal;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pinto.mythandmetal.block.ModBlocks;
import net.pinto.mythandmetal.block.customEntity.ModDungeonPortalDoorBlockEntity;
import net.pinto.mythandmetal.block.customEntity.MyBlockEntityTypes;
import net.pinto.mythandmetal.entity.AshenWolf.AshenWolfRenderer;
import net.pinto.mythandmetal.entity.CustVillager;
import net.pinto.mythandmetal.entity.ModEntites;
import net.pinto.mythandmetal.item.ModCreativeModeTabs;
import net.pinto.mythandmetal.item.ModItems;
import net.pinto.mythandmetal.mathproject.GraphCommands;
import net.pinto.mythandmetal.mixin.BlockEntityRenderersAccessor;
import net.pinto.mythandmetal.renderer.DungeonPortalRenderer;
import net.pinto.mythandmetal.renderer.GlintRenderers;
import net.pinto.mythandmetal.worldgen.biome.surface.ModSurfaceRules;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

import java.util.Map;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("mythandmetal")
public class MythandMetal
{

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mythandmetal";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    public MythandMetal()
    {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CustVillager.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntites.register(modEventBus);
        MyBlockEntityTypes.register(modEventBus);
        ModBlocks.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }



    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // this is my math project ignore it
        GraphCommands.register(event.getDispatcher());
    }



    private void commonSetup(final FMLCommonSetupEvent event)
    {


            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));


    }
    public static ShaderInstance STATIC_GLINT_SHADER;



    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.EXPLOSIVESWORD);
        }
    }



    @SubscribeEvent
    public void registerEntityRender(EntityRenderersEvent.RegisterRenderers event)
    {
        Map<BlockEntityType<?>, BlockEntityRendererProvider<?>> providers =
                BlockEntityRenderersAccessor.getProviders();
        providers.put(
                MyBlockEntityTypes.LAVA_MOD_PORTAL.get(),
                (BlockEntityRendererProvider<ModDungeonPortalDoorBlockEntity>) DungeonPortalRenderer::new);
    }



        // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntites.ASHENWOLF.get(), AshenWolfRenderer::new);

            Minecraft.getInstance().execute(GlintRenderers::register);
            

        }
    }
    

}
