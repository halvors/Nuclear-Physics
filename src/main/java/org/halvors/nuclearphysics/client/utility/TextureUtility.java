package org.halvors.nuclearphysics.client.utility;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class TextureUtility {
    public enum FluidType {
        STILL,
        FLOWING
    }

    public static TextureAtlasSprite missingIcon;
    public static Map<FluidType, Map<Fluid, TextureAtlasSprite>> textureMap = new HashMap<>();

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        /*
        for (Color color : Color.values()) {
            colors[color.ordinal()] = event.getMap().registerSprite(new ResourceLocation("mekanism:blocks/overlay/overlay_" + color.unlocalizedName));
        }

        for (TransmissionType type : TransmissionType.values()) {
            overlays.put(type, event.getMap().registerSprite(new ResourceLocation("mekanism:blocks/overlay/" + type.getTransmission() + "Overlay")));
        }
        */

        /*
        FluidRenderer.resetDisplayInts();
        RenderThermalEvaporationController.resetDisplayInts();
        RenderFluidTank.resetDisplayInts();
        */
    }

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Post event) {
        initFluidTextures(event.getMap());
    }

    public static void initFluidTextures(TextureMap map) {
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
}