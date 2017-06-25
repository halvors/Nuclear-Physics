package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.block.BlockTextured;
import org.halvors.quantum.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.common.utility.MachineUtils;

public class BlockSiren extends BlockTextured {
    public BlockSiren() {
        super("siren", Material.iron);

        setCreativeTab(Quantum.getCreativeTab());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
        if (MachineUtils.hasUsableWrench(player, x, y, z)) {
            int metadata = world.getBlockMetadata(x, y, z);

            if (player.isSneaking()) {
                metadata--;
            } else {
                metadata++;
            }

            metadata = Math.max(metadata % 16, 0);

            world.setBlockMetadataWithNotify(x, y, z, metadata, 2);

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSiren();
    }
}
