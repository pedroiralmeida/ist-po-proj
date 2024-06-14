package prr.clients;

import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;

public class BasicTariffPlan extends TariffPlan {

    public BasicTariffPlan(Client client) {
        super(client);
    }

    public double getTextCommunicationCost(TextCommunication communication) {
        int textLength = communication.getTextLength();

        if (textLength < 50) {
            if (getClient().isNormal()) { return 10; }
            if (getClient().isGold()) { return 10; }
            if (getClient().isPlatinum()) { return 0; }
        }
        if (textLength >= 50 && textLength < 100) {
            if (getClient().isNormal()) { return 16; }
            if (getClient().isGold()) { return 10; }
            if (getClient().isPlatinum()) { return 4; }
        }
        else {
            if (getClient().isNormal()) { return textLength * 2;}
            if (getClient().isGold()) { return textLength * 2; }
            if (getClient().isPlatinum()) { return 4; }
        }
        // not supposed to happen
        return -1;
    }

    public double getVoiceCommunicationCost(VoiceCommunication communication) {
       int duration = communication.getDuration();
       double cost = 0;

       if (getClient().isNormal())
           cost = duration * 20;
       if (getClient().isGold() || getClient().isPlatinum())
           cost = duration * 10;

       if (communication.isCommunicationBetweenFriends())
           cost /= 2;

       return cost;
    }

    public double getVideoCommunicationCost(VideoCommunication communication) {
        int duration = communication.getDuration();
        double cost = 0;

        if (getClient().isNormal())
            cost = duration * 30;
        if (getClient().isGold())
            cost = duration * 20;
        if (getClient().isPlatinum())
            cost = duration * 10;

        if (communication.isCommunicationBetweenFriends())
            cost /= 2;

        return cost;
    }
}
