package org.halvors.quantum.common.tile.reactor.fission;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.thermal.ThermalGrid;
import org.halvors.quantum.common.thermal.ThermalPhysics;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

import java.util.List;
import java.util.Vector;

public class TileThermometer extends TileEntity implements ITileNetwork {
    private static final int maxThreshold = 5000;
    private float detectedTemperature = ThermalPhysics.roomTemperature; // Synced
    private float previousDetectedTemperature = detectedTemperature; // Synced
    private Vector3 trackCoordinate; // Synced
    private int threshold = 1000; // Synced
    public boolean isProvidingPower = false; // Synced

    public TileThermometer() {

    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        // Server only operation.
        if (!worldObj.isRemote) {
            // Every ten ticks.
            if (worldObj.getWorldTime() % 10 == 0) {
                // Grab temperature from target or from ourselves.
                if (trackCoordinate != null) {
                    detectedTemperature = ThermalGrid.getTemperature(new VectorWorld(worldObj, trackCoordinate));
                } else {
                    detectedTemperature = ThermalGrid.getTemperature(new VectorWorld(this));
                }

                // Send update packet if temperature is different or over temperature threshold.
                if (detectedTemperature != previousDetectedTemperature || isProvidingPower != isOverThreshold()) {
                    previousDetectedTemperature = detectedTemperature;
                    isProvidingPower = isOverThreshold();
                    worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());

                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        threshold = tagCompound.getInteger("threshold");

        if (tagCompound.hasKey("trackCoordinate")) {
            trackCoordinate = new Vector3(tagCompound.getCompoundTag("trackCoordinate"));
        } else {
            trackCoordinate = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("threshold", threshold);

        if (trackCoordinate != null) {
            tagCompound.setTag("trackCoordinate", trackCoordinate.writeToNBT(new NBTTagCompound()));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        detectedTemperature = dataStream.readFloat();
        previousDetectedTemperature = dataStream.readFloat();

        if (dataStream.readBoolean()) {
            trackCoordinate = new Vector3(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
        }

        threshold = dataStream.readInt();
        isProvidingPower = dataStream.readBoolean();

    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(detectedTemperature);
        objects.add(previousDetectedTemperature);

        if (trackCoordinate != null) {
            objects.add(true);
            objects.add(trackCoordinate.intX());
            objects.add(trackCoordinate.intY());
            objects.add(trackCoordinate.intZ());
        } else {
            objects.add(false);
        }

        objects.add(threshold);
        objects.add(isProvidingPower);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3 getTrackCoordinate() {
        return trackCoordinate;
    }

    public void setTrackCoordinate(Vector3 trackCoordinate) {
        this.trackCoordinate = trackCoordinate;

        // Update the server side with this new coordinate.
        Quantum.getPacketHandler().sendToServer(new PacketTileEntity(this));
    }

    public int getThershold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold % maxThreshold;

        if (threshold <= 0) {
            this.threshold = maxThreshold;
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public float getDetectedTemperature() {
        return detectedTemperature;
    }

    public boolean isOverThreshold() {
        return detectedTemperature >= getThershold();
    }
}
