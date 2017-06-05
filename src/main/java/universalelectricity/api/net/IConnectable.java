package universalelectricity.api.net;

import net.minecraftforge.common.util.ForgeDirection;

public interface IConnectable {
    boolean canConnect(ForgeDirection direction, Object paramObject);
}