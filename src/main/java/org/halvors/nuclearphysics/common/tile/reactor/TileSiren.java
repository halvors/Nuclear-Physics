package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import org.halvors.nuclearphysics.common.block.states.BlockStateSiren;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;

public class TileSiren extends TileEntity implements ITickable {
    public TileSiren() {

    }

    @Override
    public void update() {
        if (world.getWorldTime() % 30 == 0) {
            int pitch = world.getBlockState(pos).getValue(BlockStateSiren.PITCH);

            if (world.isBlockIndirectlyGettingPowered(pos) > 0) {
                float volume = 0.5F;

                // Check in each direction for another siren block, if exists amplify volume.
                for (EnumFacing side : EnumFacing.VALUES) {
                    TileEntity tile = world.getTileEntity(pos.offset(side));

                    if (tile == this) {
                        volume *= 1.5F;
                    }
                }

                world.playSound(null, pos, ModSoundEvents.SIREN, SoundCategory.BLOCKS, volume, 1F - 0.18F * (pitch / 15F));
            }
        }
    }
}
