package prr.notifications;

import prr.clients.Client;

public class DefaultMeanOfDelivery extends MeanOfDelivery {
    public DefaultMeanOfDelivery(Client client) {
        super(client);
    }

    public void execute(Notification notification) {
        getClient().addNotification(notification);
    }
}
