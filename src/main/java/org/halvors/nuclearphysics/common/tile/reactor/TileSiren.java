package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import org.halvors.nuclearphysics.common.block.states.BlockStateSiren;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class TileSiren extends TileBase implements ITickable {
    public TileSiren() {

    }

    @Override
    public void update() {
        if (!world.isRemote && world.getWorldTime() % 30 == 0) {
            final int pitch = world.getBlockState(pos).getValue(BlockStateSiren.PITCH);

            if (world.isBlockIndirectlyGettingPowered(pos) > 0) {
                double volume = 0.5;

                // Check in each direction for another siren block, if exists amplify volume.
                for (final EnumFacing side : EnumFacing.values()) {
                    final TileEntity tile = world.getTileEntity(pos.offset(side));

                    if (tile == this) {
                        volume *= 1.5;
                    }
                }

                world.playSound(null, pos, ModSoundEvents.SIREN, SoundCategory.BLOCKS, (float) volume, 1F - 0.18F * (pitch / 15F));
            }
        }
    }
}
