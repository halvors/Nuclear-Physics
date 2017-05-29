package org.halvors.atomicscience.old.fusion;

import calclavia.lib.prefab.block.BlockTile;
import calclavia.lib.render.block.BlockRenderingHandler;
import calclavia.lib.utility.FluidUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.UniversalElectricity;

public class BlockPlasmaHeater
        extends BlockTile
{
    public BlockPlasmaHeater(int ID)
    {
        super(ID, UniversalElectricity.machine);
    }

    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        return FluidUtility.playerActivatedFluidItem(world, x, y, z, player, side);
    }

    @SideOnly(Side.CLIENT)
    public int func_71857_b()
    {
        return BlockRenderingHandler.ID;
    }

    public TileEntity func_72274_a(World var1)
    {
        return new TilePlasmaHeater();
    }

    public boolean func_71886_c()
    {
        return false;
    }

    public boolean func_71926_d()
    {
        return false;
    }
}
