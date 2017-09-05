package org.halvors.nuclearphysics.client.event;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class TextureEventHandler {
    private static final Map<FluidType, Map<Fluid, TextureAtlasSprite>> textureMap = new HashMap<>();
    private static TextureAtlasSprite missingIcon;

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        final TextureMap textureMap = event.getMap();

        textureMap.registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electric_turbine_large"));
        textureMap.registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_top"));
        textureMap.registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_middle"));
        textureMap.registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_bottom"));
        textureMap.registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_fissile_material"));
    }

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Post event) {
        final TextureMap map = event.getMap();

        missingIcon = map.getMissingSprite();
        textureMap.clear();

        for (FluidType type : FluidType.values()) {
            textureMap.put(type, new HashMap<>());
        }

        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            if (fluid.getFlowing() != null) {
                String flow = fluid.getFlowing().toString();
                TextureAtlasSprite sprite;

                if (map.getTextureExtry(flow) != null) {
                    sprite = map.getTextureExtry(flow);
                } else {
                    sprite = map.registerSprite(fluid.getStill());
                }

                textureMap.get(FluidType.FLOWING).put(fluid, sprite);
            }

            if (fluid.getStill() != null) {
                String still = fluid.getStill().toString();
                TextureAtlasSprite sprite;

                if (map.getTextureExtry(still) != null) {
                    sprite = map.getTextureExtry(still);
                } else {
                    sprite = map.registerSprite(fluid.getStill());
                }

                textureMap.get(FluidType.STILL).put(fluid, sprite);
            }
        }
    }

    public static TextureAtlasSprite getFluidTexture(Fluid fluid, FluidType type) {
        Map<Fluid, TextureAtlasSprite> map = textureMap.get(type);

        if (fluid == null || type == null) {
            return missingIcon;
        }

        return map.getOrDefault(fluid, missingIcon);
    }

    public enum FluidType {
        STILL,
        FLOWING
    }
}