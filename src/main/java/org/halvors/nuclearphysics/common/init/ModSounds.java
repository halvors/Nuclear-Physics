package org.halvors.nuclearphysics.common.init;

import org.halvors.nuclearphysics.common.Reference;

public class ModSounds {
    public static final String ACCELERATOR = createSound("block.accelerator");
    public static final String ANTIMATTER = createSound("block.antimatter");
    public static final String ASSEMBLER = createSound("block.assembler");
    public static final String ELECTRIC_TURBINE = createSound("block.electric_turbine");
    public static final String REACTOR_CELL = createSound("block.reactor_cell");
    public static final String SIREN = createSound("block.siren");

    private static String createSound(String soundName) {
        return Reference.PREFIX + soundName;
    }
}

