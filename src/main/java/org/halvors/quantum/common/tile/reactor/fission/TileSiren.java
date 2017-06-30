package org.halvors.quantum.common.tile.reactor.fission;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class TileSiren extends TileEntity implements ITickable {
    public TileSiren() {

    }

    @Override
    public void update() {
        if (world.getWorldTime() % 30 == 0) {
            //int metadata = world.getBlockState(pos).getgetBlockMetadata(xCoord, yCoord, zCoord);

            //if (world.getBlockPowerInput(xCoord, yCoord, zCoord) > 0) {
                float volume = 0.5F;

                // Check in each direction for another siren block, if exists amplify volume.
                for (int i = 0; i < 6; i++) {
                    Vector3 check = new Vector3(this).translate(EnumFacing.getOrientation(i));
                    Block block = check.getBlock(world);

                    if (block == getBlockType()) {
                        volume *= 1.5F;
                    }
                }

                // TODO
                //world.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.siren", volume, 1F - 0.18F * (metadata / 15F));
            //}
        }
    }
}
