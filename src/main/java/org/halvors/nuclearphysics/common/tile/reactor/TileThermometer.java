package org.halvors.nuclearphysics.common.tile.reactor;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalGrid;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileRotatable;
import org.halvors.nuclearphysics.common.utility.position.Position;

import javax.annotation.Nonnull;
import java.util.List;

public class TileThermometer extends TileRotatable implements ITickable {
    private static final int maxThreshold = 5000;
    private float detectedTemperature = ThermalPhysics.roomTemperature; // Synced
    private float previousDetectedTemperature = detectedTemperature; // Synced
    private Position trackCoordinate = null; // Synced
    private int threshold = 1000; // Synced
    public boolean isProvidingPower = false; // Synced

    public TileThermometer() {

    }

    @Override
    public void update() {
        // Server only operation.
        if (!world.isRemote && world.getWorldTime() % 10 == 0) {
            // Grab temperature from target or from ourselves.
            if (trackCoordinate != null) {
                detectedTemperature = ThermalGrid.getTemperature(world, trackCoordinate.getPos());
            } else {
                detectedTemperature = ThermalGrid.getTemperature(world, pos);
            }

            // Send update packet if temperature is different or over temperature threshold.
            if (detectedTemperature != previousDetectedTemperature || isProvidingPower != isOverThreshold()) {
                previousDetectedTemperature = detectedTemperature;
                isProvidingPower = isOverThreshold();
                world.notifyNeighborsOfStateChange(pos, getBlockType());

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        threshold = tag.getInteger("threshold");

        if (tag.hasKey("trackCoordinate")) {
            trackCoordinate = new Position(tag.getCompoundTag("trackCoordinate"));
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("threshold", threshold);

        if (trackCoordinate != null) {
            tag.setTag("trackCoordinate", trackCoordinate.writeToNBT(new NBTTagCompound()));
        }

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            detectedTemperature = dataStream.readFloat();
            previousDetectedTemperature = dataStream.readFloat();

            if (dataStream.readBoolean()) {
                trackCoordinate = new Position(dataStream);
            }

            threshold = dataStream.readInt();
            isProvidingPower = dataStream.readBoolean();
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
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

    public void setTrackCoordinate(Position trackCoordinate) {
        this.trackCoordinate = trackCoordinate;
    }

    public int getThershold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold % maxThreshold;

        if (threshold <= 0) {
            this.threshold = maxThreshold;
        }
    }

    public float getDetectedTemperature() {
        return detectedTemperature;
    }

    public boolean isOverThreshold() {
        return detectedTemperature >= getThershold();
    }
}
