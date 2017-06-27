package org.halvors.quantum.common.tile.reactor.fission;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class TileSiren extends TileEntity {
    public TileSiren() {

    }

    @Override
    public void updateEntity() {
        if (worldObj.getWorldTime() % 30 == 0) {
            int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

            if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0) {
                float volume = 0.5F;

                // Check in each direction for another siren block, if exists amplify volume.
                for (int i = 0; i < 6; i++) {
                    Vector3 check = new Vector3(this).translate(ForgeDirection.getOrientation(i));
                    Block block = check.getBlock(worldObj);

                    if (block == getBlockType()) {
                        volume *= 1.5F;
                    }
                }

                worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.siren", volume, 1F - 0.18F * (metadata / 15F));
            }
        }
    }
}
