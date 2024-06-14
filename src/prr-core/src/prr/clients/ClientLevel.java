package prr.clients;

import java.io.Serializable;

public abstract class ClientLevel implements Serializable {

    private static final long serialVersionUID = 202208091753L;

    private Client _client;

    public ClientLevel(Client client) {
        _client = client;
    }

    public Client getClient() {
        return _client;
    }

    public abstract boolean isNormal();
    public abstract boolean isGold();
    public abstract boolean isPlatinum();


    public abstract String toString();

}
