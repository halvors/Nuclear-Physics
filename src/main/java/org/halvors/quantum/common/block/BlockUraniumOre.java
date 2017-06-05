package org.halvors.quantum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;

import java.util.Random;

public class BlockUraniumOre extends BlockRadioactive {
    public BlockUraniumOre() {
        super();

        setUnlocalizedName("oreUranium");
        setTextureName(Reference.PREFIX + "oreUranium");
        //setStepSound(soundStoneFootstep);
        setHardness(2.0F);
        
        isRandomlyRadioactive = ConfigurationManager.General.allowRadioactiveOres;
        canWalkPoison = ConfigurationManager.General.allowRadioactiveOres;
        canSpread = false;
        radius = 1.0F;
        amplifier = 0;
        spawnParticle = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (ConfigurationManager.General.allowRadioactiveOres) {
            super.randomDisplayTick(world, x, y, z, random);
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
