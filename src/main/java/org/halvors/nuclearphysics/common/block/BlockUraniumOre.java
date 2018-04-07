package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.ConfigurationManager;

import java.util.Random;

public class BlockUraniumOre extends BlockRadioactive {
    public BlockUraniumOre() {
        super("uranium_ore", Material.rock);

        //setStepSound(SoundType.STONE); // TODO: Port this to 1.7.10
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 2);

        isRandomlyRadioactive = canWalkPoison = spawnParticle = ConfigurationManager.General.allowRadioactiveOres;
        canSpread = false;
        radius = 1.0F;
        amplifier = 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
