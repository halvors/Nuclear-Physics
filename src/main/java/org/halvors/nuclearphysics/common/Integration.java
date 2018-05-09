package org.halvors.nuclearphysics.common;

import cpw.mods.fml.common.Loader;

public class Integration {
    public static final String BUILDCRAFT_CORE_ID = "buildcraftcore";
    public static final String COFH_CORE_ID = "cofhcore";
    public static final String MEKANISM_ID = "mekanism";
    public static final String REDSTONE_FLUX_ID = "redstoneflux";

    public static boolean isBuildcraftLoaded;
    public static boolean isCOFHCoreLoaded;
    public static boolean isMekanismLoaded;
    public static boolean isRedstoneFluxLoaded;

    public static void initialize() {
        isBuildcraftLoaded = Loader.isModLoaded(BUILDCRAFT_CORE_ID);
        isCOFHCoreLoaded = Loader.isModLoaded(COFH_CORE_ID);
        isMekanismLoaded = Loader.isModLoaded(MEKANISM_ID);
        isRedstoneFluxLoaded = Loader.isModLoaded(REDSTONE_FLUX_ID);
    }
}
