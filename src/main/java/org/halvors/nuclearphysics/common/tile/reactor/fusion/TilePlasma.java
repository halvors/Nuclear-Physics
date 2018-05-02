package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalGrid;
import org.halvors.nuclearphysics.common.type.Position;

public class TilePlasma extends TileEntity {
    public static final int plasmaMaxTemperature = 1000000;
    private int temperature = plasmaMaxTemperature;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            Position pos = new Position(xCoord, yCoord, zCoord);
            ThermalGrid.addTemperature(worldObj, pos, (temperature - ThermalGrid.getTemperature(worldObj, pos) * 0.1F));

            if (worldObj.getWorldTime() % 20 == 0) {
                temperature /= 1.5;

                if (temperature <= plasmaMaxTemperature / 10) {
                    // At this temperature, set block to fire.
                    worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.fire);
                } else {
                    for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                        // Randomize spread direction.
                        if (worldObj.rand.nextFloat() < 0.4) {
                            final Position spreadPos = new Position(xCoord, yCoord, zCoord).offset(side);
                            final TileEntity tile = worldObj.getTileEntity(spreadPos.getIntX(), spreadPos.getIntY(), spreadPos.getIntZ());

                            if (!(tile instanceof TilePlasma)) {
                                MinecraftForge.EVENT_BUS.post(new PlasmaSpawnEvent(worldObj, spreadPos.getIntX(), spreadPos.getIntY(), spreadPos.getIntZ(), temperature));
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
