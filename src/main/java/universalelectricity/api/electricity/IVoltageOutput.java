package universalelectricity.api.electricity;

import net.minecraftforge.common.util.ForgeDirection;

public interface IVoltageOutput {
    long getVoltageOutput(ForgeDirection paramForgeDirection);
}
