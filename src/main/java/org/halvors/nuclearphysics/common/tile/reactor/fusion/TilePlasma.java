package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalGrid;

public class TilePlasma extends TileEntity implements ITickable {
    public static final int PLASMA_MAX_TEMPERATURE = 1000000;

    private int temperature = PLASMA_MAX_TEMPERATURE;

    @Override
    public void update() {
        if (!world.isRemote) {
            ThermalGrid.addTemperature(world, pos, (temperature - ThermalGrid.getTemperature(world, pos)) * 0.1F);

            if (world.getWorldTime() % 20 == 0) {
                temperature /= 1.5;

                if (temperature <= PLASMA_MAX_TEMPERATURE / 10) {
                    // At this temperature, set block to fire.
                    world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                } else {
                    for (final EnumFacing side : EnumFacing.values()) {
                        // Randomize spread direction.
                        if (world.rand.nextFloat() < 0.4) {
                            final BlockPos spreadPos = pos.offset(side);
                            final TileEntity tile = world.getTileEntity(spreadPos);

                            if (!(tile instanceof TilePlasma)) {
                                MinecraftForge.EVENT_BUS.post(new PlasmaSpawnEvent(world, spreadPos, temperature));
                            }
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }
}
