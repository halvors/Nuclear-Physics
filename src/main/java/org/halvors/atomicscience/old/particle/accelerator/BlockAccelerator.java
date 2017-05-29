package org.halvors.atomicscience.old.particle.accelerator;

import atomicscience.AtomicScience;
import calclavia.lib.prefab.block.BlockRotatable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.UniversalElectricity;

public class BlockAccelerator
        extends BlockRotatable
{
    public BlockAccelerator(int id)
    {
        super(id, UniversalElectricity.machine);
    }

    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.func_72805_g(x, y, z);
        if (!par1World.field_72995_K) {
            par5EntityPlayer.openGui(AtomicScience.instance, 0, par1World, x, y, z);
        }
        return true;
    }

    public TileEntity func_72274_a(World var1)
    {
        return new TileAccelerator();
    }
}
