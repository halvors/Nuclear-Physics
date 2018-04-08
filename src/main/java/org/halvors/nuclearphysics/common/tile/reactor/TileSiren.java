package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.init.ModSounds;
import org.halvors.nuclearphysics.common.type.Position;

public class TileSiren extends TileEntity {
    public TileSiren() {

    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote && worldObj.getWorldTime() % 30 == 0) {
            int pitch = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

            if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0) {
                float volume = 0.5F;

                // Check in each direction for another siren block, if exists amplify volume.
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    TileEntity tile = new Position(xCoord, yCoord, zCoord).offset(side).getTileEntity(worldObj);

                    if (tile == this) {
                        volume *= 1.5F;
                    }
                }

                worldObj.playSoundEffect(xCoord, yCoord, zCoord, ModSounds.SIREN, volume, 1F - 0.18F * (pitch / 15F));
            }
        }
    }
}
