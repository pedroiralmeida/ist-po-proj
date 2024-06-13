package prr.communications;

import prr.clients.Client;
import prr.clients.TariffPlan;
import prr.terminals.Terminal;

import java.io.Serializable;

public abstract class Communication implements Serializable {

    private static final long serialVersionUID = 202208091753L;

    private int _key;
    private double _cost;
    private Terminal _originTerminal;
    private Terminal _destinationTerminal;
    private boolean _ongoing = false;
    private boolean _paid = false;

    public Communication(int id, Terminal originTerminal, Terminal destinationTerminal) {
        _key = id;
        _originTerminal = originTerminal;
        _destinationTerminal = destinationTerminal;
    }

    public Communication() {}

    public void setCost(double cost) {
        _cost = cost;
    }

    public int getKey() {
        return _key;
    }
    public double getCost() {
        return _cost;
    }
    public Terminal getOriginTerminal() {
        return _originTerminal;
    }
    public Terminal getDestinationTerminal() {
        return _destinationTerminal;
    }
    public boolean isOngoing() {
        return _ongoing;
    }
    public boolean hasBeenPaid() {
        return _paid;
    }

    public Client getSenderClient() {
        return _originTerminal.getClient();
    }

    public TariffPlan getSenderClientTariffPlan() {
        return getSenderClient().getTariffPlan();
    }

    public void start() {
        _ongoing = true;
    }
    public void end() {
        _ongoing = false;
    }

    public boolean isCommunicationBetweenFriends() {
        return _originTerminal.isFriendsWith(_destinationTerminal);
    }

    public abstract boolean isTextCommunication();
    public abstract boolean isVideoCommunication();

    public abstract void calculateAndRegisterCost();

    public abstract String toString();
}
