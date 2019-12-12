package org.halvors.nuclearphysics.client.event;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class TextureEventHandler {
    private static final Map<EnumFluidType, Map<Fluid, TextureAtlasSprite>> FLUID_TEXTURE_MAP = new HashMap<>();
    private static final Map<String, TextureAtlasSprite> TEXTURE_MAP = new HashMap<>();
    private static TextureAtlasSprite missingIcon;

    private static final ResourceLocation ELECTRIC_TURBINE_LARGE = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "electric_turbine_large");
    private static final ResourceLocation REACTOR_FISSILE_MATERIAL = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "reactor_fissile_material");

    /*
    @SubscribeEvent
    public static void onPreTextureStitchEvent(final TextureStitchEvent.Pre event) {
        final TextureMap map = event.getMap();

        map.registerSprite(ELECTRIC_TURBINE_LARGE);
        map.registerSprite(REACTOR_FISSILE_MATERIAL);
        TEXTURE_MAP.put("reactor_fissile_material", map.getTextureExtry(REACTOR_FISSILE_MATERIAL.toString()));
    }

    @SubscribeEvent
    public static void onPostTextureStitchEvent(final TextureStitchEvent.Post event) {
        final TextureMap map = event.getMap();

        missingIcon = map.getMissingSprite();
        FLUID_TEXTURE_MAP.clear();

        for (EnumFluidType type : EnumFluidType.values()) {
            FLUID_TEXTURE_MAP.put(type, new HashMap<>());
        }

        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            if (fluid.getStill() != null) {
                FLUID_TEXTURE_MAP.get(EnumFluidType.STILL).put(fluid, map.getTextureExtry(fluid.getStill().toString()));
            }

            if (fluid.getFlowing() != null) {
                FLUID_TEXTURE_MAP.get(EnumFluidType.FLOWING).put(fluid, map.getTextureExtry(fluid.getFlowing().toString()));
            }
        }
    }
    */

    public static TextureAtlasSprite getFluidTexture(final Fluid fluid, final EnumFluidType type) {
        final Map<Fluid, TextureAtlasSprite> map = FLUID_TEXTURE_MAP.get(type);

        if (fluid == null || type == null) {
            return missingIcon;
        }

        return map.getOrDefault(fluid, missingIcon);
    }

    public static TextureAtlasSprite getTexture(final String texture) {
        return TEXTURE_MAP.getOrDefault(texture, missingIcon);
    }

    public enum EnumFluidType {
        STILL,
        FLOWING
    }
}