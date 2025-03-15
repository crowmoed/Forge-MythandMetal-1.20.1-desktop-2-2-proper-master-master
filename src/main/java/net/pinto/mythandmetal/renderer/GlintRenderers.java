package net.pinto.mythandmetal.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class GlintRenderers extends RenderType {


    public static final ResourceLocation ARMTIERSEVEN_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierseven_glint_enchanted.png");
    public static final ResourceLocation ARMTIERSIX_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tiersix_glint_enchanted.png");


    public static final ResourceLocation ARMTIERFIVE_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierfive_glint_enchanted.png");
    public static final ResourceLocation ARMTIERFOUR_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierfour_glint_enchanted.png");
    public static final ResourceLocation ARMTIERTHREA_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierthrea_glint_enchanted.png");
    public static final ResourceLocation ARMTIERTWO_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted.png");


    public static final ResourceLocation TIERSEVEN_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierseven_glint_enchanted.png");
    public static final ResourceLocation TIERSIX_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tiersix_glint_enchanted.png");



    public static final ResourceLocation TIERFIVE_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierfive_glint_enchanted.png");
    public static final ResourceLocation TIERFOUR_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierfour_glint_enchanted.png");
    public static final ResourceLocation TIERTHREA_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tierthrea_glint_enchanted.png");


    public static final ResourceLocation TIERSEVEN_LOCATION2 = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted7.png");
    public static final ResourceLocation TIERSIX_LOCATION2 = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted6.png");
    public static final ResourceLocation TIERFIVE_LOCATION2 = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted5.png");
    public static final ResourceLocation TIERFOUR_LOCATION2 = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted4.png");
    public static final ResourceLocation TIERTHREA_LOCATION2 = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted3.png");
    public static final ResourceLocation TIERTWO_LOCATION2 = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted2.png");

    public static final ResourceLocation TIERTWO_LOCATION = new ResourceLocation("mythandmetal:textures/misc/tiertwo_glint_enchanted.png");

    public static final ResourceLocation ENCH1_LOCATION = new ResourceLocation("mythandmetal:textures/misc/enchanted_glint_item1.png");
    public static final ResourceLocation ENCH2_LOCATION = new ResourceLocation("mythandmetal:textures/misc/enchanted_glint_item2.png");
    public static final ResourceLocation ENCH3_LOCATION = new ResourceLocation("mythandmetal:textures/misc/enchanted_glint_item3.png");
    public static final ResourceLocation ENCH4_LOCATION = new ResourceLocation("mythandmetal:textures/misc/enchanted_glint_item4.png");



    public static final ResourceLocation shaderLocation1 = new ResourceLocation( "mythandmetal:rendertype_glint_custom");
    public static final ResourceLocation shaderLocation2 = new ResourceLocation( "mythandmetal:rendertype_armor_glint2");
    public static final ResourceLocation shaderLocation3 = new ResourceLocation( "mythandmetal:rendertype_armor_entity_glint2");

    private static ShaderInstance customShader;
    private static ShaderInstance customArmShader;
    private static ShaderInstance customArmShaderEntity;


    public static void register(){
        try {
            customShader = new ShaderInstance(Minecraft.getInstance().getResourceManager(), shaderLocation1, DefaultVertexFormat.POSITION_TEX);
            customArmShader = new ShaderInstance(Minecraft.getInstance().getResourceManager(), shaderLocation2, DefaultVertexFormat.POSITION_TEX);
            customArmShaderEntity = new ShaderInstance(Minecraft.getInstance().getResourceManager(), shaderLocation3, DefaultVertexFormat.POSITION_TEX);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ShaderInstance getCustomShader() {
        return customShader;
    }
    public static ShaderInstance getCustomArmShader() {
        return customArmShader;
    }
    public static ShaderInstance getCustomArmShaderEntity() {
        return customArmShaderEntity;
    }

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GLINT_SHADER_CUSTOM = new RenderStateShard.ShaderStateShard(GlintRenderers::getCustomShader);
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GLINT_ARMSHADER_CUSTOM = new RenderStateShard.ShaderStateShard(GlintRenderers::getCustomArmShader);
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GLINT_ARMSHADER_CUSTOMENTITY = new RenderStateShard.ShaderStateShard(GlintRenderers::getCustomArmShaderEntity);


    public GlintRenderers(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
    private static final RenderType TIERSEVEN_GLINT = create("tierseven_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSEVEN_GLINT_DIRECT = create("tierseven_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSEVEN_ENTITY_GLINT_DIRECT = create("tierseven_entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSEVEN_ENTITY_GLINT = create("tierseven_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setOutputState(ITEM_ENTITY_TARGET).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSEVEN_ARMOR_ENTITY_GLINT = create("tierseven_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ARMTIERSEVEN_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));







    private static final RenderType TIERSIX_GLINT = create("tiersix_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSIX_GLINT_DIRECT = create("tiersix_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSIX_ENTITY_GLINT_DIRECT = create("tiersix_entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSIX_ENTITY_GLINT = create("tiersix_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setOutputState(ITEM_ENTITY_TARGET).setTexturingState(RenderStateShard.DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSIX_ARMOR_ENTITY_GLINT = create("tiersix_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ARMTIERSIX_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(DEFAULT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));

    private static final RenderType TIERFIVE_GLINT = create("tierfive_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFIVE_GLINT_DIRECT = create("tierfive_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFIVE_ENTITY_GLINT_DIRECT = create("tierfive_entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFIVE_ENTITY_GLINT = create("tierfive_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setOutputState(ITEM_ENTITY_TARGET).setTexturingState(ENTITY_GLINT_TEXTURING).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFIVE_ARMOR_ENTITY_GLINT = create("tierfive_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ARMTIERFIVE_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));




    private static final RenderType TIERFOUR_GLINT = create("tierfour_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(RenderStateShard.NO_TRANSPARENCY
    ).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFOUR_GLINT_DIRECT = create("tierfour_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFOUR_ENTITY_GLINT_DIRECT = create("tierfour_entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(NO_TRANSPARENCY
    ).setTexturingState(DEFAULT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFOUR_ENTITY_GLINT = create("tierfour_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setOutputState(ITEM_ENTITY_TARGET).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERFOUR_ARMOR_ENTITY_GLINT = create("tierfour_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ARMTIERFOUR_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));




    private static final RenderType TIERTHREA_GLINT = create("tierthrea_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTHREA_GLINT_DIRECT = create("tierthrea_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTHREA_ENTITY_GLINT_DIRECT = create("tierthrea_entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY
    ).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTHREA_ENTITY_GLINT = create("tierthrea_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setOutputState(ITEM_ENTITY_TARGET).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTHREA_ARMOR_ENTITY_GLINT = create("tierthrea_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ARMTIERTHREA_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));

    private static final RenderType TIERTWO_GLINT = create("tiertwo_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTWO_GLINT_DIRECT = create("tiertwo_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTWO_ENTITY_GLINT_DIRECT = create("tiertwo_entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTWO_ENTITY_GLINT = create("tiertwo_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setOutputState(ITEM_ENTITY_TARGET).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERTWO_ARMOR_ENTITY_GLINT = create("tiertwo_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setTransparencyState(GLINT_TRANSPARENCY
    ).setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ARMTIERTWO_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));


    private static final RenderType TIERTWO_ARMOR_ENTITY_GLINT_OUTLINE = create("tiertwo_armor_entity_glint_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERTWO_GLINT_DIRECT_OUTLINE = create("tiertwo_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERTWO_ENTITY_GLINT_DIRECT_OUTLINE = create("tiertwo_entity_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTWO_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERTHREA_ARMOR_ENTITY_GLINT_OUTLINE = create("tierthrea_armor_entity_glint_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).createCompositeState(false));

    private static final RenderType TIERTHREA_GLINT_DIRECT_OUTLINE = create("tierthrea_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).setTransparencyState(GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERTHREA_ENTITY_GLINT_DIRECT_OUTLINE = create("tierthrea_entity_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERTHREA_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY
    ).setTexturingState(ENTITY_GLINT_TEXTURING).setTransparencyState(GLINT_TRANSPARENCY).createCompositeState(false));


    private static final RenderType TIERFOUR_ARMOR_ENTITY_GLINT_OUTLINE = create("tierfour_armor_entity_glint_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTransparencyState(GLINT_TRANSPARENCY).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERFOUR_GLINT_DIRECT_OUTLINE = create("tierfour_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERFOUR_ENTITY_GLINT_DIRECT_OUTLINE = create("tierfour_entity_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFOUR_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).setTransparencyState(GLINT_TRANSPARENCY).createCompositeState(false));



    private static final RenderType TIERFIVE_ARMOR_ENTITY_GLINT_OUTLINE = create("tierfive_armor_entity_glint_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setTransparencyState(GLINT_TRANSPARENCY
    ).setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERFIVE_GLINT_DIRECT_OUTLINE = create("tierfive_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(GLINT_TEXTURING).setTransparencyState(GLINT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType TIERFIVE_ENTITY_GLINT_DIRECT_OUTLINE = create("tierfive_entity_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERFIVE_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));



    private static final RenderType TIERSIX_ARMOR_ENTITY_GLINT_OUTLINE = create("tiersix_armor_entity_glint_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setTransparencyState(GLINT_TRANSPARENCY
    ).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));

    private static final RenderType TIERSIX_GLINT_DIRECT_OUTLINE = create("tiersix_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSIX_ENTITY_GLINT_DIRECT_OUTLINE = create("tiersix_entity_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSIX_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY
    ).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));


    private static final RenderType TIERSEVEN_ARMOR_ENTITY_GLINT_OUTLINE = create("tierseven_armor_entity_glint_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(GLINT_TRANSPARENCY
    ).createCompositeState(false));

    private static final RenderType TIERSEVEN_GLINT_DIRECT_OUTLINE = create("tierseven_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY
    ).setTexturingState(GLINT_TEXTURING).createCompositeState(false));
    private static final RenderType TIERSEVEN_ENTITY_GLINT_DIRECT_OUTLINE = create("tierseven_entity_glint_direct_outline", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TIERSEVEN_LOCATION2, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY
    ).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));



    public static final RenderType OUTLINE = create("custoutline", DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS, 256,true,true,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GLINT_SHADER)
                    .createCompositeState(false));


    private static final RenderType ARMGLINT_DIRECTENCHANTMENT1 = create("tierfive_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ENCH1_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));
    private static final RenderType ARMGLINT_DIRECTENCHANTMENT2 = create("tierfive_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ENCH2_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));
    private static final RenderType ARMGLINT_DIRECTENCHANTMENT3 = create("tierfive_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ENCH3_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));
    private static final RenderType ARMGLINT_DIRECTENCHANTMENT4 = create("tierfive_armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false ,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ENCH4_LOCATION, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));

    private static final RenderType GLINT_DIRECTENCHANTMENT1 = create("glint_direct1", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).setTextureState(new RenderStateShard.TextureStateShard(ENCH1_LOCATION, true, false)).createCompositeState(false));
    private static final RenderType GLINT_DIRECTENCHANTMENT1_DIRECT = create("entity_glint_direct1", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).setTextureState(new RenderStateShard.TextureStateShard(ENCH1_LOCATION, true, false)).createCompositeState(false));

    private static final RenderType GLINT_DIRECTENCHANTMENT2 = create("glint_direct2", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).setTextureState(new RenderStateShard.TextureStateShard(ENCH2_LOCATION, true, false)).createCompositeState(false));
    private static final RenderType GLINT_DIRECTENCHANTMENT2_DIRECT = create("entity_glint_direct2", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTextureState(new RenderStateShard.TextureStateShard(ENCH2_LOCATION, true, false)).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));

    private static final RenderType GLINT_DIRECTENCHANTMENT3 = create("glint_direct3", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).setTextureState(new RenderStateShard.TextureStateShard(ENCH3_LOCATION, true, false)).createCompositeState(false));
    private static final RenderType GLINT_DIRECTENCHANTMENT3_DIRECT = create("entity_glint_direct3", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTextureState(new RenderStateShard.TextureStateShard(ENCH3_LOCATION, true, false)).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));

    private static final RenderType GLINT_DIRECTENCHANTMENT4 = create("glint_direct4", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).setTextureState(new RenderStateShard.TextureStateShard(ENCH4_LOCATION, true, false)).createCompositeState(false));
    private static final RenderType GLINT_DIRECTENCHANTMENT4_DIRECT = create("entity_glint_direct4", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,false,false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).setTextureState(new RenderStateShard.TextureStateShard(ENCH4_LOCATION, true, false)).createCompositeState(false));

    public static RenderType getglint_direct4() {return GLINT_DIRECTENCHANTMENT4;}
    public static RenderType getentity_glint_direct4() {return GLINT_DIRECTENCHANTMENT4_DIRECT;}

    public static RenderType getglint_direct3() {return GLINT_DIRECTENCHANTMENT3;}
    public static RenderType getentity_glint_direct3() {return GLINT_DIRECTENCHANTMENT3_DIRECT;}

    public static RenderType getglint_direct2() {return GLINT_DIRECTENCHANTMENT2;}
    public static RenderType getentity_glint_direct2() {return GLINT_DIRECTENCHANTMENT2_DIRECT;}

    public static RenderType getglint_direct1() {return GLINT_DIRECTENCHANTMENT1;}
    public static RenderType getentity_glint_direct1() {return GLINT_DIRECTENCHANTMENT1_DIRECT;}

    public static RenderType getarmentity_glint_direct1() {return ARMGLINT_DIRECTENCHANTMENT1;}
    public static RenderType getarmentity_glint_direct2() {return ARMGLINT_DIRECTENCHANTMENT2;}
    public static RenderType getarmentity_glint_direct3() {return ARMGLINT_DIRECTENCHANTMENT3;}
    public static RenderType getarmentity_glint_direct4() {return ARMGLINT_DIRECTENCHANTMENT4;}




    public static RenderType getTierSevenarmorEntityGlint() {return TIERSEVEN_ARMOR_ENTITY_GLINT;}
    public static RenderType getTierSevenEntityGlintDirect() {
        return TIERSEVEN_ENTITY_GLINT_DIRECT;
    }
    public static RenderType getTierSevenGlintDirect() {
        return TIERSEVEN_GLINT_DIRECT;
    }


    public static RenderType getTierSixarmorEntityGlint() {
        return TIERSIX_ARMOR_ENTITY_GLINT;
    }
    public static RenderType getTierSixEntityGlintDirect() {
        return TIERSIX_ENTITY_GLINT_DIRECT;
    }
    public static RenderType getTierSixGlintDirect() {
        return TIERSIX_GLINT_DIRECT;
    }

    public static RenderType getTierFivearmorEntityGlint() {
        return TIERFIVE_ARMOR_ENTITY_GLINT;
    }
    public static RenderType getTierFiveEntityGlintDirect() {
        return TIERFIVE_ENTITY_GLINT_DIRECT;
    }
    public static RenderType getTierFiveGlintDirect() {
        return TIERFIVE_GLINT_DIRECT;
    }

    public static RenderType getTierFourarmorEntityGlint() {
        return TIERFOUR_ARMOR_ENTITY_GLINT;
    }
    public static RenderType getTierFourEntityGlintDirect() {
        return TIERFOUR_ENTITY_GLINT_DIRECT;
    }
    public static RenderType getTierFourGlintDirect() {
        return TIERFOUR_GLINT_DIRECT;
    }

    public static RenderType getTierThreaArmorEntityGlint() {return TIERTHREA_ARMOR_ENTITY_GLINT;}
    public static RenderType getTierThreaEntityGlintDirect() {return TIERTHREA_ENTITY_GLINT_DIRECT;}
    public static RenderType getTierThreaGlintDirect() {return TIERTHREA_GLINT_DIRECT;}

    public static RenderType getTierTwoArmorEntityGlint() {return TIERTWO_ARMOR_ENTITY_GLINT;}
    public static RenderType getTierTwoEntityGlintDirect() {return TIERTWO_ENTITY_GLINT_DIRECT;}
    public static RenderType getTierTwoGlintDirect() {return TIERTWO_GLINT_DIRECT;}











    public static RenderType getTierTwoArmorEntityGlintOutline() {return TIERTWO_ARMOR_ENTITY_GLINT_OUTLINE;}
    public static RenderType getTierTwoEntityGlintDirectOutline() {return TIERTWO_ENTITY_GLINT_DIRECT_OUTLINE;}
    public static RenderType getTierTwoGlintDirectOutline() {return TIERTWO_GLINT_DIRECT_OUTLINE;}

    public static RenderType getTierThreaArmorEntityGlintOutline() {return TIERTHREA_ARMOR_ENTITY_GLINT_OUTLINE;}
    public static RenderType getTierThreaEntityGlintDirectOutline() {return TIERTHREA_ENTITY_GLINT_DIRECT_OUTLINE;}
    public static RenderType getTierThreaGlintDirectOutline() {return TIERTHREA_GLINT_DIRECT_OUTLINE;}
    

    public static RenderType getTierSixArmorEntityGlintOutline() {return TIERSIX_ARMOR_ENTITY_GLINT_OUTLINE;}
    public static RenderType getTierSixEntityGlintDirectOutline() {return TIERSIX_ENTITY_GLINT_DIRECT_OUTLINE;}
    public static RenderType getTierSixGlintDirectOutline() {return TIERSIX_GLINT_DIRECT_OUTLINE;}
    
    
    public static RenderType getTierSevenArmorEntityGlintOutline() {return TIERSEVEN_ARMOR_ENTITY_GLINT_OUTLINE;}
    public static RenderType getTierSevenEntityGlintDirectOutline() {return TIERSEVEN_ENTITY_GLINT_DIRECT_OUTLINE;}
    public static RenderType getTierSevenGlintDirectOutline() {return TIERSEVEN_GLINT_DIRECT_OUTLINE;}

    public static RenderType getTierFourarmorEntityGlintOutline() {
        return TIERFOUR_ARMOR_ENTITY_GLINT_OUTLINE;
    }
    public static RenderType getTierFourEntityGlintDirectOutline() {
        return TIERFOUR_ENTITY_GLINT_DIRECT_OUTLINE;
    }
    public static RenderType getTierFourGlintDirectOutline() {
        return TIERFOUR_GLINT_DIRECT_OUTLINE;
    }

    public static RenderType getTierFivearmorEntityGlintOutline() {
        return TIERFIVE_ARMOR_ENTITY_GLINT_OUTLINE;
    }
    public static RenderType getTierFiveEntityGlintDirectOutline() {
        return TIERFIVE_ENTITY_GLINT_DIRECT_OUTLINE;
    }
    public static RenderType getTierFiveGlintDirectOutline() {
        return TIERFIVE_GLINT_DIRECT_OUTLINE;
    }


    public static RenderType getOutlineGlint() {return OUTLINE_GLINT;}

    private static final RenderType OUTLINE_GLINT = RenderType.create(
            "outline_glint",
            DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.LINES, // LINES mode for an outline
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            new ResourceLocation("your_mod_id", "textures/misc/outline_glint.png"),
                            true,
                            false
                    ))
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .setTexturingState(RenderStateShard.GLINT_TEXTURING)
                    .createCompositeState(false)
    );

}
