package universalelectricity.api.electricity;

import net.minecraftforge.common.util.ForgeDirection;

public interface IVoltageInput {
    long getVoltageInput(ForgeDirection direction);

    void onWrongVoltage(ForgeDirection direction, long paramLong);
}