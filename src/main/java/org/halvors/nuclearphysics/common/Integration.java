package org.halvors.nuclearphysics.common;

import cpw.mods.fml.common.Loader;

public class Integration {
    public static final String BUILDCRAFT_CORE_ID = "buildcraftcore";
    public static final String COFH_CORE_ID = "CoFHCore";
    public static final String MEKANISM_ID = "Mekanism"; // Confirmed right for 1.7.10.
    public static final String IC2_ID = "IC2"; // Confirmed right for 1.7.10.

    public static boolean isBuildcraftLoaded;
    public static boolean isCOFHCoreLoaded;
    public static boolean isMekanismLoaded;
    public static boolean isRedstoneFluxLoaded;
    public static boolean isIC2Loaded;

    public static void initialize() {
        isBuildcraftLoaded = Loader.isModLoaded(BUILDCRAFT_CORE_ID);
        isCOFHCoreLoaded = Loader.isModLoaded(COFH_CORE_ID);
        isMekanismLoaded = Loader.isModLoaded(MEKANISM_ID);
        isIC2Loaded = Loader.isModLoaded(IC2_ID);
    }
}
