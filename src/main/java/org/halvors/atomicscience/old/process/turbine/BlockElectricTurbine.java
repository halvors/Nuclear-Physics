package org.halvors.atomicscience.old.process.turbine;

import calclavia.lib.prefab.turbine.BlockTurbine;
import calclavia.lib.render.block.BlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockElectricTurbine
        extends BlockTurbine
{
    public BlockElectricTurbine(int id)
    {
        super(id, Material.field_76243_f);
        func_111022_d("atomicscience:machine");
    }

    @SideOnly(Side.CLIENT)
    public int func_71857_b()
    {
        return BlockRenderingHandler.ID;
    }

    public TileEntity func_72274_a(World var1)
    {
        return new TileElectricTurbine();
    }
}
