package org.halvors.nuclearphysics.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.init.ModFluids;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TextureEventHandler {
    private static final Map<String, IIcon> textureMap = new HashMap<>();

    public static void registerIcon(final String name, final TextureMap map) {
        textureMap.put(name, map.registerIcon(Reference.PREFIX + name));
    }

    public static void registerIcon(final Fluid fluid, final TextureMap map) {
        textureMap.put(fluid.getName(), map.registerIcon(Reference.PREFIX + "fluids/" + fluid.getName() + "_still"));
    }

    public static IIcon getIcon(final String name) {
        return textureMap.get(name);
    }

    private static void setIcon(final Fluid fluid) {
        fluid.setIcons(textureMap.get("fluids/" + fluid.getName() + "_still"));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitchEventPre(final TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) {
            registerIcon("electromagnet_edge", event.map);
            registerIcon("fulmination_generator_edge", event.map);
            registerIcon("gas_funnel_edge", event.map);
            registerIcon(ModFluids.deuterium, event.map);
            registerIcon(ModFluids.steam, event.map);
            registerIcon(ModFluids.tritium, event.map);
            registerIcon(ModFluids.uraniumHexaflouride, event.map);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitchEventPost(final TextureStitchEvent.Post event) {
        ModFluids.toxicWaste.setIcons(ModFluids.toxicWaste.getBlock().getIcon(0, 0));
        ModFluids.plasma.setIcons(ModFluids.plasma.getBlock().getIcon(0, 0));

        setIcon(ModFluids.deuterium);
        setIcon(ModFluids.uraniumHexaflouride);
        setIcon(ModFluids.steam);
        setIcon(ModFluids.tritium);
    }
}