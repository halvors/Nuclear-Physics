package org.halvors.quantum.common.block.reactor.fusion;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.reactor.fusion.TileFusionReactor;
import org.halvors.quantum.lib.prefab.block.BlockTile;
import org.halvors.quantum.lib.render.BlockRenderingHandler;
import org.halvors.quantum.lib.utility.FluidUtility;

public class BlockFusionReactor extends BlockTile {
    public BlockFusionReactor() {
        super(Material.iron);

        setCreativeTab(Quantum.getCreativeTab());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return FluidUtility.playerActivatedFluidItem(world, x, y, z, player, side);
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
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getId();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFusionReactor();
    }
}

