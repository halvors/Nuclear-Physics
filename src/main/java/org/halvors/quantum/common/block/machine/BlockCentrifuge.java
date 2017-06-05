package org.halvors.quantum.common.block.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.machine.TileCentrifuge;
import org.halvors.quantum.lib.prefab.block.BlockRotatable;
import org.halvors.quantum.lib.render.block.BlockRenderingHandler;

public class BlockCentrifuge extends BlockRotatable {
    public BlockCentrifuge() {
        super(Material.iron);

        setUnlocalizedName("centrifuge");
        setCreativeTab(Quantum.getCreativeTab());
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (!world.isRemote) {
            player.openGui(Quantum.getInstance(), 0, world, x, y, z);

            return true;
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCentrifuge();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.ID;
    }
}
