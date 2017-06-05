package universalelectricity.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyContainer {
    void setEnergy(ForgeDirection direction, long paramLong);

    long getEnergy(ForgeDirection direction);

    long getEnergyCapacity(ForgeDirection direction);
}
