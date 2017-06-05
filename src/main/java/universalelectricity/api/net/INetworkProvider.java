package universalelectricity.api.net;

public interface INetworkProvider<N> {
    N getNetwork();

    void setNetwork(N network);
}