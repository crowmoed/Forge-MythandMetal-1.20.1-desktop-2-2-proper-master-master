package net.pinto.mythandmetal.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class CustomShaderManager {

    // Your custom shaders
    private static ShaderInstance customShader;

    // Initialize shaders, make sure it's done early in the game loop
    public static void initializeShaders(ResourceProvider resourceProvider) throws IOException {


            // Shader location, replace with your actual shader path
            ResourceLocation shaderLocation = new ResourceLocation("minecraft", "rendertype_glint_custom");

            // Load and create the shader
            customShader = new ShaderInstance(resourceProvider, shaderLocation, DefaultVertexFormat.POSITION_TEX);

            // Check if the shader is successfully loaded

    }






    public static ShaderInstance getCustomShader() {
        return customShader;
    }
}