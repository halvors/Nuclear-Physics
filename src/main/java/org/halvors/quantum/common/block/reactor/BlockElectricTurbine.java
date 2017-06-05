package org.halvors.quantum.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.TileElectricTurbine;
import org.halvors.quantum.lib.prefab.turbine.BlockTurbine;
import org.halvors.quantum.lib.render.block.BlockRenderingHandler;

public class BlockElectricTurbine extends BlockTurbine {
    public BlockElectricTurbine() {
        super(Material.iron);

        setUnlocalizedName("electricTurbine");
        setTextureName(Reference.PREFIX + "machine");
        setCreativeTab(Quantum.getCreativeTab());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileElectricTurbine();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.ID;
    }
}
