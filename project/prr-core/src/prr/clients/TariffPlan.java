package prr.clients;

import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;

import java.io.Serializable;

public abstract class TariffPlan implements Serializable {

    private static final long serialVersionUID = 202208091753L;

    private String _name;
    private Client _client;

    public TariffPlan(Client client) {
        _client = client;
    }

    public Client getClient() {
        return _client;
    }

    public abstract double getTextCommunicationCost(TextCommunication communication);
    public abstract double getVoiceCommunicationCost(VoiceCommunication communication);
    public abstract double getVideoCommunicationCost(VideoCommunication communication);
}
