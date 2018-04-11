package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.ConfigurationManager;

import java.util.Random;

public class BlockUraniumOre extends BlockRadioactive {
    public BlockUraniumOre() {
        super("uranium_ore", Material.ROCK);

        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 2);

        canSpread = false;
        radius = 1.0F;
        amplifier = 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
