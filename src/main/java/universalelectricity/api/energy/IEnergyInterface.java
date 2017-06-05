package universalelectricity.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.net.IConnectable;

public interface IEnergyInterface extends IConnectable {
    long onReceiveEnergy(ForgeDirection direction, long paramLong, boolean paramBoolean);

    long onExtractEnergy(ForgeDirection direction, long paramLong, boolean paramBoolean);
}
