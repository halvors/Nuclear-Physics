package org.halvors.quantum.common.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.client.render.BlockRenderingHandler;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockQuantum;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.quantum.common.utility.FluidUtility;

public class BlockPlasmaHeater extends BlockQuantum {
    public BlockPlasmaHeater() {
        super("plasmaHeater", Material.iron);

        setTextureName(Reference.PREFIX + "machine");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return FluidUtility.playerActivatedFluidItem(world, x, y, z, player, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TilePlasmaHeater();
    }
}

