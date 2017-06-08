package org.halvors.quantum.common.block.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.lib.prefab.block.BlockRotatable;
import org.halvors.quantum.lib.render.BlockRenderingHandler;

public class BlockNuclearBoiler extends BlockRotatable {
    public BlockNuclearBoiler() {
        super(Material.iron);

        setUnlocalizedName("nuclearBoiler");
        setCreativeTab(Quantum.getCreativeTab());
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
        return BlockRenderingHandler.ID;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Quantum.getInstance(), 0, world, x, y, z);

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int metadata) {
        return new TileNuclearBoiler();
    }
}
