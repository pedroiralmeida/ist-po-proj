package prr.notifications;

import prr.clients.Client;

import java.io.Serializable;
import java.util.*;

public class NotificationService implements Serializable {

    private static final long serialVersionUID = 202208091753L;

    private List<Client> _clients = new ArrayList<>();

    public void subscribe(Client client) {
        if (!_clients.contains(client))
            _clients.add(client);
    }

    public void notify(Notification notification) {
        for (Client client : _clients)
            client.update(notification);

        _clients.clear();
    }
}
