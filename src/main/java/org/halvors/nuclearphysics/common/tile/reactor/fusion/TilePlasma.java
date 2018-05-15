package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.science.grid.ThermalGrid;
import org.halvors.nuclearphysics.common.type.Position;

public class TilePlasma extends TileEntity {
    private static final String NBT_TEMPERATURE = "temperature";
    public static final int PLASMA_MAX_TEMPERATURE = 1000000;

    private int temperature = PLASMA_MAX_TEMPERATURE;

    public TilePlasma() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        temperature = tag.getInteger(NBT_TEMPERATURE);
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger(NBT_TEMPERATURE, temperature);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            final Position pos = new Position(xCoord, yCoord, zCoord);

            ThermalGrid.addTemperature(worldObj, pos, (temperature - ThermalGrid.getTemperature(worldObj, pos)) * 0.1);

            if (worldObj.getWorldTime() % 20 == 0) {
                temperature /= 1.5;

                if (temperature <= PLASMA_MAX_TEMPERATURE / 10) {
                    // At this temperature, set block to fire.
                    worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.fire);
                } else {
                    for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                        // Randomize spread direction.
                        if (worldObj.rand.nextFloat() < 0.4) {
                            final Position spreadPos = new Position(xCoord, yCoord, zCoord).offset(side);
                            TileEntity tile = spreadPos.getTileEntity(worldObj);

                            if (!(tile instanceof TilePlasma)) {
                                final PlasmaSpawnEvent event = new PlasmaSpawnEvent(worldObj, spreadPos.getIntX(), spreadPos.getIntY(), spreadPos.getIntZ(), temperature);
                                MinecraftForge.EVENT_BUS.post(event);

                                if (!event.isCanceled()) {
                                    // Replacing block with plasma.
                                    worldObj.setBlock(spreadPos.getIntX(), spreadPos.getIntY(), spreadPos.getIntZ(), ModFluids.plasma.getBlock());

                                    // We need to update the tile entity with the one from the plasma block that didn't exist before.
                                    final TileEntity spreadTile = spreadPos.getTileEntity(worldObj);

                                    if (spreadTile instanceof TilePlasma) {
                                        temperature = event.getTemperature();
                                    }
                                }
                            }

                            // Get the tile entity from position again, it might have changed.
                            tile = spreadPos.getTileEntity(worldObj);

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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }
}
