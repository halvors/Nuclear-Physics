package org.halvors.atomicscience.old.fission.reactor;

import calclavia.lib.content.module.TileBlock;
import calclavia.lib.prefab.vector.Cuboid;
import net.minecraft.block.material.Material;

public class TileControlRod extends TileBlock
{
    public TileControlRod()
    {
        super(Material.field_76243_f);
        this.bounds = new Cuboid(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 1.0D, 0.699999988079071D);
        this.isOpaqueCube = false;
    }
}
