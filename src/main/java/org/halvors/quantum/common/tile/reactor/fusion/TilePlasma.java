package org.halvors.quantum.common.tile.reactor.fusion;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.event.PlasmaEvent;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.common.thermal.ThermalGrid;

public class TilePlasma extends TileEntity {
    public static int plasmaMaxTemperature = 1000000;
    private int temperature = plasmaMaxTemperature;

    @Override
    public void updateEntity() {
        super.updateEntity();

        ThermalGrid.addTemperature(new VectorWorld(this), (temperature - ThermalGrid.getTemperature(new VectorWorld(this))) * 0.1F);

        if (worldObj.getWorldTime() % 20 == 0) {
            temperature /= 1.5;

            if (temperature <= plasmaMaxTemperature / 10) {
                // At this temperature, set block to fire.
                worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.fire, 0, 3);

                // We manually trigger a block update, to avoid client glitches.
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            } else {
                for (int i = 0; i < 6; i++) {
                    // Randomize spread direction.
                    if (worldObj.rand.nextFloat() < 0.4) {
                        Vector3 position = new Vector3(this);
                        position.translate(ForgeDirection.getOrientation(i));

                        TileEntity tileEntity = position.getTileEntity(worldObj);

                        if (!(tileEntity instanceof TilePlasma)) {
                            MinecraftForge.EVENT_BUS.post(new PlasmaEvent.PlasmaSpawnEvent(worldObj, position.intX(), position.intY(), position.intZ(), temperature));
                        }
                    }
                }
            }
        }
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
