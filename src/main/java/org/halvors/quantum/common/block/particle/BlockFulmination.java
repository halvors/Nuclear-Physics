package org.halvors.quantum.common.block.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.particle.TileFulmination;

public class BlockFulmination extends BlockContainer {
    public BlockFulmination() {
        super(Material.iron);

        setUnlocalizedName("fulmination");
        setTextureName(Reference.PREFIX + "fulmination");
        setCreativeTab(Quantum.getCreativeTab());
        setHardness(10);
        setResistance(25000);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }


    /*
    @Override
    @SideOnly(Side.CLIENT)
    protected TileRender newRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
    */

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileFulmination();
    }
}
