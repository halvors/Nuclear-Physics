package org.halvors.quantum.common.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Reference;

import java.util.Random;

public class BlockRadioactiveGrass extends BlockRadioactive {
    private static IIcon iconTop;
    private static IIcon iconBottom;

    public BlockRadioactiveGrass() {
        super("radioactiveGrass");

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
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + getUnlocalizedName().replace("tile.", "") + "_top");
        iconBottom = iconRegister.registerIcon(Reference.PREFIX + getUnlocalizedName().replace("tile.", "") + "_bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
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
    public int quantityDropped(Random random) {
        return 0;
    }
}
