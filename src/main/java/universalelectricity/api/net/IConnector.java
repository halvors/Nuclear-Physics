package universalelectricity.api.net;

import net.minecraftforge.common.util.ForgeDirection;

public interface IConnector<N> extends INetworkProvider<N>, IConnectable {
    Object[] getConnections();

    IConnector<N> getInstance(ForgeDirection paramForgeDirection);
}

