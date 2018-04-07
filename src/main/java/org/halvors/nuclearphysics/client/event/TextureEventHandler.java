package org.halvors.nuclearphysics.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.init.ModFluids;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TextureEventHandler {
    private static final Map<FluidType, Map<Fluid, IIcon>> fluidTextureMap = new HashMap<>();
    private static final Map<String, IIcon> textureMap = new HashMap<>();

    public static void registerIcon(String name, TextureMap map) {
        textureMap.put(name, map.registerIcon(name));
    }

    public static IIcon getIcon(String name) {
        return textureMap.get(name);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void preTextureHook(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) {
            registerIcon(Reference.PREFIX + "electromagnet_edge", event.map);
            registerIcon(Reference.PREFIX + "fulmination_generator_edge", event.map);
            registerIcon(Reference.PREFIX + "gas_funnel_edge", event.map);

            registerFluidIcon(ModFluids.deuterium, event.map);
            registerFluidIcon(ModFluids.steam, event.map);
            registerFluidIcon(ModFluids.tritium, event.map);
            registerFluidIcon(ModFluids.uraniumHexaflouride, event.map);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void postTextureHook(TextureStitchEvent.Post event) {
        ModFluids.toxicWaste.setIcons(ModFluids.toxicWaste.getBlock().getIcon(0, 0));
        ModFluids.plasma.setIcons(ModFluids.plasma.getBlock().getIcon(0, 0));

        setFluidIcon(ModFluids.deuterium);
        setFluidIcon(ModFluids.uraniumHexaflouride);
        setFluidIcon(ModFluids.steam);
        setFluidIcon(ModFluids.tritium);
    }

    private void registerFluidIcon(Fluid fluid, TextureMap map) {
        NuclearPhysics.getLogger().info("Registering texture for " + fluid.getName() + ", path: " + Reference.PREFIX + fluid.getStillIcon());

        registerIcon(Reference.PREFIX + "fluids/" + fluid.getName() + "_still", map);
    }

    private void setFluidIcon(Fluid fluid) {
        fluid.setIcons(textureMap.get(Reference.PREFIX + "fluids/" + fluid.getName() + "_still"));
    }

    /*
    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        final TextureMap map = event.getMap();

        map.registerSprite(electric_turbine_large);
        map.registerSprite(reactorFissileMaterial);
        textureMap.put("reactor_fissile_material", map.getTextureExtry(reactorFissileMaterial.toString()));
    }

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Post event) {
        final TextureMap map = event.getMap();

        missingIcon = map.getMissingSprite();
        fluidTextureMap.clear();

        for (FluidType type : FluidType.values()) {
            fluidTextureMap.put(type, new HashMap<>());
        }

        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            if (fluid.getStill() != null) {
                fluidTextureMap.get(FluidType.STILL).put(fluid, map.getTextureExtry(fluid.getStill().toString()));
            }

            if (fluid.getFlowing() != null) {
                fluidTextureMap.get(FluidType.FLOWING).put(fluid, map.getTextureExtry(fluid.getFlowing().toString()));
            }
        }
    }

    public static TextureAtlasSprite getFluidTexture(Fluid fluid, FluidType type) {
        Map<Fluid, TextureAtlasSprite> map = fluidTextureMap.get(type);

        if (fluid == null || type == null) {
            return missingIcon;
        }

        return map.getOrDefault(fluid, missingIcon);
    }

    public static TextureAtlasSprite getTexture(String texture) {
        return textureMap.getOrDefault(texture, missingIcon);
    }
    */

    public enum FluidType {
        STILL,
        FLOWING
    }
}