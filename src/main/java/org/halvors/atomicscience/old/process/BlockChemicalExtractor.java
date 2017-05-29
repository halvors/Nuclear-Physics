package org.halvors.atomicscience.old.process;

import atomicscience.AtomicScience;
import calclavia.lib.prefab.block.BlockRotatable;
import calclavia.lib.render.block.BlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.UniversalElectricity;

public class BlockChemicalExtractor extends BlockRotatable
{
    public BlockChemicalExtractor(int ID)
    {
        super(ID, UniversalElectricity.machine);
    }

    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.func_72805_g(x, y, z);
        if (!par1World.field_72995_K)
        {
            par5EntityPlayer.openGui(AtomicScience.instance, 0, par1World, x, y, z);
            return true;
        }
        return true;
    }

    public TileEntity func_72274_a(World var1)
    {
        return new TileChemicalExtractor();
    }

    public boolean func_71886_c()
    {
        return false;
    }

    public boolean func_71926_d()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int func_71857_b()
    {
        return BlockRenderingHandler.ID;
    }
}
