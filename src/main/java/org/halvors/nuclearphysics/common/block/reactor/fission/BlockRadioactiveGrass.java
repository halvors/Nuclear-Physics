package org.halvors.nuclearphysics.common.block.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockRadioactive;

import java.util.Random;

public class BlockRadioactiveGrass extends BlockRadioactive {
    @SideOnly(Side.CLIENT)
    private static IIcon iconTop, iconBottom;

    public BlockRadioactiveGrass() {
        super("radioactive_grass", Material.grass);

        setHardness(0.2F);

        canSpread = true;
        radius = 5;
        amplifier = 2;
        canWalkPoison = true;
        isRandomlyRadioactive = true;
        spawnParticle = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + name + "_top");
        iconBottom = iconRegister.registerIcon(Reference.PREFIX + name + "_bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        switch (side) {
            case 0:
                return iconBottom;

            case 1:
                return iconTop;

            default:
                return blockIcon;
        }
    }

    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
}
