package prr.notifications;

import prr.clients.Client;

import java.io.Serializable;

public abstract class MeanOfDelivery implements Serializable {

    private static final long serialVersionUID = 202208091753L;

    private Client _client;

    public MeanOfDelivery(Client client) {
        _client = client;
    }

    public Client getClient() {
        return _client;
    }

    public abstract void execute(Notification notification);
}
