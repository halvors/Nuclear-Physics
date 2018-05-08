package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.science.grid.ThermalGrid;

public class TilePlasma extends TileEntity implements ITickable {
    public static final int PLASMA_MAX_TEMPERATURE = 1000000;

    private int temperature = PLASMA_MAX_TEMPERATURE;

    @Override
    public void update() {
        if (!world.isRemote) {
            ThermalGrid.addTemperature(world, pos, (temperature - ThermalGrid.getTemperature(world, pos)) * 0.1);

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
                            TileEntity tile = world.getTileEntity(spreadPos);

                            if (!(tile instanceof TilePlasma)) {
                                final PlasmaSpawnEvent event = new PlasmaSpawnEvent(world, spreadPos, temperature);
                                MinecraftForge.EVENT_BUS.post(event);

                                if (!event.isCanceled()) {
                                    // Replacing block with plasma.
                                    world.setBlockState(spreadPos, ModFluids.plasma.getBlock().getDefaultState());

                                    // We need to update the tile entity with the one from the plasma block that didn't exist before.
                                    final TileEntity spreadTile = world.getTileEntity(spreadPos);

                                    if (spreadTile instanceof TilePlasma) {
                                        temperature = event.getTemperature();
                                    }
                                }
                            }

                            // Get the tile entity from position again, it might have changed.
                            tile = world.getTileEntity(spreadPos);

                            if (tile instanceof TilePlasma) {
                                ((TilePlasma) tile).setTemperature(temperature);
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
