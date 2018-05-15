package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.block.BlockContainerBase;
import org.halvors.nuclearphysics.common.tile.reactor.TileSiren;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

public class BlockSiren extends BlockContainerBase {
    public BlockSiren() {
        super("siren", Material.iron);

        setHardness(0.6F);
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int facing, final float playerX, final float playerY, final float playerZ) {
        final BlockPos pos = new BlockPos(x, y, z);

        if (WrenchUtility.hasUsableWrench(player, pos)) {
            int pitch = pos.getBlockMetadata(world);

            if (player.isSneaking()) {
                pitch--;
            } else {
                pitch++;
            }

            return world.setBlockMetadataWithNotify(x, y, z, Math.max(pitch % 16, 0), 2);
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileSiren();
    }
}
