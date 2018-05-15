package org.halvors.nuclearphysics.common.tile.reactor;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.science.grid.ThermalGrid;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.tile.TileRotatable;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.List;

public class TileThermometer extends TileRotatable {
    private static final String NBT_THRESHOLD = "threshold";
    private static final String NBT_TRACK_COORDINATE = "trackCoordinate";
    private static final int MAX_THRESHOLD = 5000;

    private double detectedTemperature = ThermalPhysics.ROOM_TEMPERATURE; // Synced
    private double previousDetectedTemperature = detectedTemperature; // Synced

    private Position trackCoordinate = null; // Synced
    private int threshold = 1000; // Synced
    public boolean isProvidingPower = false; // Synced

    public TileThermometer() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        threshold = tag.getInteger(NBT_THRESHOLD);

        if (tag.hasKey(NBT_TRACK_COORDINATE)) {
            trackCoordinate = new Position(tag.getCompoundTag(NBT_TRACK_COORDINATE));
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger(NBT_THRESHOLD, threshold);

        if (trackCoordinate != null) {
            tag.setTag(NBT_TRACK_COORDINATE, trackCoordinate.writeToNBT(new NBTTagCompound()));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote && worldObj.getWorldTime() % 10 == 0) {
            // Grab temperature from target or from ourselves.
            if (trackCoordinate != null) {
                detectedTemperature = ThermalGrid.getTemperature(worldObj, new BlockPos(trackCoordinate.getIntX(), trackCoordinate.getIntY(), trackCoordinate.getIntZ()));
            } else {
                detectedTemperature = ThermalGrid.getTemperature(worldObj, pos);
            }

            // Send update packet if temperature is different or over temperature threshold.
            if (detectedTemperature != previousDetectedTemperature || isProvidingPower != isOverThreshold()) {
                previousDetectedTemperature = detectedTemperature;
                isProvidingPower = isOverThreshold();
                
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            detectedTemperature = dataStream.readDouble();
            previousDetectedTemperature = dataStream.readDouble();

            if (dataStream.readBoolean()) {
                trackCoordinate = new Position(dataStream);
            }

            threshold = dataStream.readInt();
            isProvidingPower = dataStream.readBoolean();
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        super.getPacketData(objects);

        objects.add(detectedTemperature);
        objects.add(previousDetectedTemperature);

        if (trackCoordinate != null) {
            objects.add(true);
            trackCoordinate.getPacketData(objects);
        } else {
            objects.add(false);
        }

        objects.add(threshold);
        objects.add(isProvidingPower);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position getTrackCoordinate() {
        return trackCoordinate;
    }

    public void setTrackCoordinate(final Position trackCoordinate) {
        this.trackCoordinate = trackCoordinate;
    }

    public int getThershold() {
        return threshold;
    }

    public void setThreshold(final int threshold) {
        this.threshold = threshold % MAX_THRESHOLD;

        if (threshold <= 0) {
            this.threshold = MAX_THRESHOLD;
        }
    }

    public double getDetectedTemperature() {
        return detectedTemperature;
    }

    public boolean isOverThreshold() {
        return detectedTemperature >= getThershold();
    }
}
