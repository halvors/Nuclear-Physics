package org.halvors.quantum.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.halvors.quantum.common.ConfigurationManager;

import java.util.Random;

public class BlockUraniumOre extends BlockRadioactive {
    public BlockUraniumOre() {
        super("uranium_ore", Material.ROCK);

        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 2);

        isRandomlyRadioactive = ConfigurationManager.General.allowRadioactiveOres;
        canWalkPoison = ConfigurationManager.General.allowRadioactiveOres;
        canSpread = false;
        radius = 1.0F;
        amplifier = 0;
        spawnParticle = ConfigurationManager.General.allowRadioactiveOres;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
