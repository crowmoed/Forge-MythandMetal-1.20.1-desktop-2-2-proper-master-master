package net.pinto.mythandmetal.mixin;


import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.pinto.mythandmetal.renderer.GlintRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.SortedMap;

import static net.minecraft.client.renderer.MultiBufferSource.immediateWithBuffers;


@Mixin(value = RenderBuffers.class)
public class RenderBuffersMixin {

    private static MultiBufferSource.BufferSource savedBufferSource = null;

    private static boolean hasRendered = false;

    @Inject(at = @At(value = "TAIL"), method = "bufferSource", cancellable = true)
    private void injectBufferSource(CallbackInfoReturnable<MultiBufferSource.BufferSource> cbk) {

        if (savedBufferSource == null) {
            System.out.println("First Override");
            RenderBuffers renderBuffers = ((RenderBuffers)(Object)this);

            SortedMap<RenderType, BufferBuilder> fixedBuffers = ((renderBufferInterface) renderBuffers).getFixedBuffers();

            put(fixedBuffers, GlintRenderers.getglint_direct1());
            put(fixedBuffers, GlintRenderers.getentity_glint_direct1());
            put(fixedBuffers, GlintRenderers.getglint_direct2());
            put(fixedBuffers, GlintRenderers.getentity_glint_direct2());
            put(fixedBuffers, GlintRenderers.getglint_direct3());
            put(fixedBuffers, GlintRenderers.getentity_glint_direct3());
            put(fixedBuffers, GlintRenderers.getglint_direct4());
            put(fixedBuffers, GlintRenderers.getentity_glint_direct4());

            put(fixedBuffers, GlintRenderers.getTierFivearmorEntityGlint());
            put(fixedBuffers, GlintRenderers.getTierFiveGlintDirect());
            put(fixedBuffers, GlintRenderers.getTierFiveEntityGlintDirect());

            put(fixedBuffers, GlintRenderers.getTierFourarmorEntityGlint());
            put(fixedBuffers, GlintRenderers.getTierFourGlintDirect());
            put(fixedBuffers, GlintRenderers.getTierFourEntityGlintDirect());

            put(fixedBuffers, GlintRenderers.getTierThreaArmorEntityGlint());
            put(fixedBuffers, GlintRenderers.getTierThreaGlintDirect());
            put(fixedBuffers, GlintRenderers.getTierThreaEntityGlintDirect());

            put(fixedBuffers, GlintRenderers.getTierTwoArmorEntityGlint());
            put(fixedBuffers, GlintRenderers.getTierTwoGlintDirect());
            put(fixedBuffers, GlintRenderers.getTierTwoEntityGlintDirect());

            put(fixedBuffers, GlintRenderers.getTierSixarmorEntityGlint());
            put(fixedBuffers, GlintRenderers.getTierSixGlintDirect());
            put(fixedBuffers, GlintRenderers.getTierSixEntityGlintDirect());

            put(fixedBuffers, GlintRenderers.getTierSixArmorEntityGlintOutline());
            put(fixedBuffers, GlintRenderers.getTierSixGlintDirectOutline());
            put(fixedBuffers, GlintRenderers.getTierSixEntityGlintDirectOutline());


            put(fixedBuffers, GlintRenderers.getTierSevenarmorEntityGlint());
            put(fixedBuffers, GlintRenderers.getTierSevenGlintDirect());
            put(fixedBuffers, GlintRenderers.getTierSevenEntityGlintDirect());

            put(fixedBuffers, GlintRenderers.getTierSevenArmorEntityGlintOutline());
            put(fixedBuffers, GlintRenderers.getTierSevenGlintDirectOutline());
            put(fixedBuffers, GlintRenderers.getTierSevenEntityGlintDirectOutline());


            put(fixedBuffers, GlintRenderers.getTierFivearmorEntityGlintOutline());
            put(fixedBuffers, GlintRenderers.getTierFiveGlintDirectOutline());
            put(fixedBuffers, GlintRenderers.getTierFiveEntityGlintDirectOutline());

            put(fixedBuffers, GlintRenderers.getTierFourarmorEntityGlintOutline());
            put(fixedBuffers, GlintRenderers.getTierFourGlintDirectOutline());
            put(fixedBuffers, GlintRenderers.getTierFourEntityGlintDirectOutline());

            put(fixedBuffers, GlintRenderers.getTierThreaArmorEntityGlintOutline());
            put(fixedBuffers, GlintRenderers.getTierThreaGlintDirectOutline());
            put(fixedBuffers, GlintRenderers.getTierThreaEntityGlintDirectOutline());

            put(fixedBuffers, GlintRenderers.getTierTwoArmorEntityGlintOutline());
            put(fixedBuffers, GlintRenderers.getTierTwoGlintDirectOutline());
            put(fixedBuffers, GlintRenderers.getTierTwoEntityGlintDirectOutline());
            savedBufferSource = immediateWithBuffers(fixedBuffers, new BufferBuilder(256));
        }
        cbk.setReturnValue(savedBufferSource);
    }



    private static void put(SortedMap<RenderType, BufferBuilder> p_110102_, RenderType p_110103_) {
        p_110102_.put(p_110103_, new BufferBuilder(p_110103_.bufferSize()));
    }
}
