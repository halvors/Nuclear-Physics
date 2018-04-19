package org.halvors.nuclearphysics.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.block.BlockRadioactive;

public class BlockUraniumOre extends BlockRadioactive {
    public BlockUraniumOre() {
        super("uranium_ore", Material.rock);

        setHardness(2);
        setHarvestLevel("pickaxe", 2);

        canSpread = false;
        radius = 1;
        amplifier = 0;
    }
}
