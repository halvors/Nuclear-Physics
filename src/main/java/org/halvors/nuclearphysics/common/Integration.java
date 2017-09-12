package org.halvors.nuclearphysics.common;

import net.minecraftforge.fml.common.Loader;

public class Integration {
    public static final String MEKANISM_MOD_ID = "Mekanism";

    public static boolean isMekanismLoaded = false;

    public static void initialize() {
        isMekanismLoaded = Loader.isModLoaded(MEKANISM_MOD_ID);
    }
}
