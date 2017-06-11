package universalelectricity.api.energy;

import net.minecraft.nbt.*;

public class EnergyStorageHandler {
    protected long energy;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;
    protected long lastEnergy;

    public EnergyStorageHandler() {
        this(0L);
    }

    public EnergyStorageHandler(long capacity) {
        this(capacity, capacity, capacity);
    }

    public EnergyStorageHandler(long capacity, long maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public EnergyStorageHandler(long capacity, long maxReceive, long maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public EnergyStorageHandler readFromNBT(NBTTagCompound nbt) {
        NBTBase energyTag = nbt.getTag("energy");

        if (energyTag instanceof NBTTagDouble) {
            this.energy = (long) ((NBTTagDouble) energyTag).getDouble();
        } else if (energyTag instanceof NBTTagFloat) {
            this.energy = (long) ((NBTTagFloat) energyTag).getFloat();
        } else if (energyTag instanceof NBTTagInt) {
            this.energy = ((NBTTagInt) energyTag).getInt();
        } else if (energyTag instanceof NBTTagLong) {
            this.energy = ((NBTTagLong) energyTag).getLong();
        }

        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("energy", getEnergy());

        return nbt;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;

        if (getEnergy() > capacity) {
            this.energy = capacity;
        }
    }

    public void setMaxTransfer(long maxTransfer) {
        setMaxReceive(maxTransfer);
        setMaxExtract(maxTransfer);
    }

    public void setMaxReceive(long maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(long maxExtract) {
        this.maxExtract = maxExtract;
    }

    public long getMaxReceive() {
        return this.maxReceive;
    }

    public long getMaxExtract() {
        return this.maxExtract;
    }

    public void setEnergy(long energy) {
        this.energy = energy;

        if (getEnergy() > getEnergyCapacity()) {
            this.energy = getEnergyCapacity();
        } else if (getEnergy() < 0L) {
            this.energy = 0L;
        }
    }

    public void modifyEnergyStored(long energy) {
        setEnergy(getEmptySpace() + energy);
        if (getEnergy() > getEnergyCapacity()) {
            setEnergy(getEnergyCapacity());
        } else if (getEnergy() < 0L) {
            setEnergy(0L);
        }
    }

    public long receiveEnergy(long receive, boolean doReceive) {
        long energyReceived = Math.min(getEnergyCapacity() - getEnergy(), Math.min(getMaxReceive(), receive));

        if (doReceive) {
            this.lastEnergy = getEnergy();
            setEnergy(getEnergy() + energyReceived);
        }

        return energyReceived;
    }

    public long receiveEnergy(boolean doReceive) {
        return receiveEnergy(getMaxReceive(), doReceive);
    }

    public long receiveEnergy() {
        return receiveEnergy(true);
    }

    public long extractEnergy(long extract, boolean doExtract) {
        long energyExtracted = Math.min(getEnergy(), Math.min(getMaxExtract(), extract));

        if (doExtract) {
            this.lastEnergy = getEnergy();
            setEnergy(getEnergy() - energyExtracted);
        }

        return energyExtracted;
    }

    public long extractEnergy(boolean doExtract) {
        return extractEnergy(getMaxExtract(), doExtract);
    }

    public long extractEnergy() {
        return extractEnergy(true);
    }

    public boolean checkReceive(long receive) {
        return receiveEnergy(receive, false) >= receive;
    }

    public boolean checkReceive() {
        return checkReceive(getMaxReceive());
    }

    public boolean checkExtract(long extract) {
        return extractEnergy(extract, false) >= extract;
    }

    public boolean checkExtract() {
        return checkExtract(getMaxExtract());
    }

    public boolean isFull() {
        return getEnergy() >= getEnergyCapacity();
    }

    public boolean isEmpty() {
        return getEnergy() == 0L;
    }

    public long getLastEnergy() {
        return this.lastEnergy;
    }

    public boolean didEnergyStateChange() {
        return ((getLastEnergy() == 0L) && (getEnergy() > 0L)) || ((getLastEnergy() > 0L) && (getEnergy() == 0L));
    }

    public long getEmptySpace() {
        return getEnergyCapacity() - getEnergy();
    }

    public long getEnergy() {
        return this.energy;
    }

    public long getEnergyCapacity() {
        return this.capacity;
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + getEnergy() + "/" + getEnergyCapacity() + "]";
    }
}
