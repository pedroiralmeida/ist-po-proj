package prr.clients;
import prr.communications.Communication;
import prr.notifications.DefaultMeanOfDelivery;
import prr.notifications.MeanOfDelivery;
import prr.notifications.Notification;
import prr.terminals.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.ListIterator;

public class Client implements Serializable {

    private static final long serialVersionUID = 202208091753L;

    private String _key;
    private String _name;
    private int _taxID;
    private TariffPlan _tariffPlan;
    private boolean _notificationsEnabled = true;
    private ClientLevel _clientLevel;
    private int numberOfTerminals = 0;
    private List<Terminal> _terminals = new ArrayList<>();
    private MeanOfDelivery _meanOfDelivery = new DefaultMeanOfDelivery(this);
    private List<Notification> _notifications = new ArrayList<>();

    public Client(String key, String name, int taxID)  {
        _key = key;
        _name = name;
        _taxID = taxID;
        _clientLevel = new NormalLevel(this);
        _tariffPlan = new BasicTariffPlan(this);
    }

    public void setClientLevel(ClientLevel clientLevel) {
        _clientLevel = clientLevel;
    }
    public void setNotificationsActive(boolean notificationsActive) {
        _notificationsEnabled = notificationsActive;
    }

    public boolean hasNotificationsEnabled() {
        return _notificationsEnabled;
    }
    public String getKey() {
        return _key;
    }
    public String getName() {
        return _name;
    }
    public int getTaxID() {
        return _taxID;
    }
    public TariffPlan getTariffPlan() {
        return _tariffPlan;
    }

    public double getTotalPayments() {
        double total = 0;

        for (Terminal terminal : _terminals)
            for (double payment: terminal.getPaymentsMade())
                total += payment;

        return total;
    }
    public double getTotalDebt() {
        double total = 0;

        for (Terminal terminal : _terminals)
            for (double debt: terminal.getDebts())
                total += debt;

        return total;
    }
    public double getBalance() {
        return getTotalPayments() - getTotalDebt();
    }

    public List<Communication> getRealizedCommunications() {
        List<Communication> realizedCommunications = new ArrayList<>();

        for (Terminal terminal : _terminals)
            realizedCommunications.addAll(terminal.getRealizedCommunications());

        return realizedCommunications;
    }
    public List<Communication> getReceivedCommunications() {
        List<Communication> receivedCommunications = new ArrayList<>();

        for (Terminal terminal : _terminals)
            receivedCommunications.addAll(terminal.getReceivedCommunications());

        return receivedCommunications;
    }

    public boolean isNormal() {
        return _clientLevel.isNormal();
    }
    public boolean isGold() {
        return _clientLevel.isGold();
    }
    public boolean isPlatinum() {
        return _clientLevel.isPlatinum();
    }

    public void updateToNormal() {
        setClientLevel(new NormalLevel(this));
    }
    public void updateToGold() {
        setClientLevel(new GoldLevel(this));
    }
    public void updateToPlatinum() {
        setClientLevel(new PlatinumLevel(this));
    }

    public void tryToUpdateClient() {
        ListIterator<Communication> receivedCommunicationsItr =
                getReceivedCommunications().listIterator(getReceivedCommunications().size());
        int numOfIterations = 2;

        if (getBalance() < 0) {
            updateToNormal();
            return;
        }
        if (isPlatinum()) {
            while (numOfIterations-- > 0) {
                if (!receivedCommunicationsItr.previous().isTextCommunication())
                    return;
            }
            updateToGold();
        }
        if (isGold()) {
            numOfIterations = 5;
            while (numOfIterations-- > 0) {
                if (!receivedCommunicationsItr.previous().isVideoCommunication())
                    return;
            }
            updateToPlatinum();
        }
    }

    public void addTerminal(Terminal terminal) {
        _terminals.add(terminal);
        numberOfTerminals++;
    }

    public void addNotification(Notification notification) {
        _notifications.add(notification);
    }

    public void cleanNotifications()  {
        _notifications.clear();
    }

    public void update(Notification notification) {
        _meanOfDelivery.execute(notification);
    }

    public String getNotificationsToString() {
        StringBuilder notificationsToString = new StringBuilder();

        for (Notification notification : _notifications) {
            notificationsToString.append("\n");
            notificationsToString.append(notification);
        }
        return notificationsToString.toString();
    }

    public String toString() {
        return "CLIENT" + "|" + getKey() + "|" + getName() + "|" + getTaxID() + "|" +
                _clientLevel + "|" + (hasNotificationsEnabled() ? "YES" : "NO") + "|" +
                numberOfTerminals + "|" + Math.round(getTotalPayments()) + "|" +
                Math.round(getTotalDebt());
    }
}
