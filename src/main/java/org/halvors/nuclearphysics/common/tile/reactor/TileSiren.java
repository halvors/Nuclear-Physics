package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.init.ModSounds;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class TileSiren extends TileBase {
    public TileSiren() {

    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote && worldObj.getWorldTime() % 30 == 0) {
            int pitch = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

            if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0) {
                float volume = 0.5F;

                // Check in each direction for another siren block, if exists amplify volume.
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    final TileEntity tile = pos.offset(side).getTileEntity(worldObj);

                    if (tile == this) {
                        volume *= 1.5F;
                    }
                }

                worldObj.playSoundEffect(xCoord, yCoord, zCoord, ModSounds.SIREN, volume, 1F - 0.18F * (pitch / 15F));
            }
        }
    }
}
