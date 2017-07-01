package org.halvors.quantum.common.tile.reactor.fission;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileSiren extends TileEntity implements ITickable {
    public TileSiren() {

    }

    @Override
    public void update() {
        if (world.getWorldTime() % 30 == 0) {
            //int metadata = world.getBlockState(pos).getgetBlockMetadata(xCoord, yCoord, zCoord);

            if (world.isBlockIndirectlyGettingPowered(pos) > 0) {
                float volume = 0.5F;

                // Check in each direction for another siren block, if exists amplify volume.
                for (EnumFacing side : EnumFacing.VALUES) {
                    Block block = world.getBlockState(pos.offset(side)).getBlock();

                    if (block == blockType) {
                        volume *= 1.5F;
                    }
                }

                // TODO
                //world.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.siren", volume, 1F - 0.18F * (metadata / 15F));
            }
        }
    }
}
