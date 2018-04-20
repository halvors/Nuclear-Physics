package org.halvors.nuclearphysics.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.block.BlockRadioactive;

public class BlockUraniumOre extends BlockRadioactive {
    public BlockUraniumOre() {
        super("uranium_ore", Material.ROCK);

        setHardness(2.0F);
        setHarvestLevel("pickaxe", 2);

        canSpread = false;
        radius = 1.0F;
        amplifier = 0;
    }
}
