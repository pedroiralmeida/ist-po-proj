package prr.communications;

import prr.terminals.Terminal;

public class VideoCommunication extends InteractiveCommunication {

    public VideoCommunication(int id, Terminal originTerminal, Terminal destinationTerminal) {
        super(id, originTerminal, destinationTerminal);
    }

    @Override
    public void calculateAndRegisterCost() {
        double cost = getSenderClientTariffPlan().getVideoCommunicationCost(this);

        setCost(cost);
        getOriginTerminal().addDebt(cost);
        getSenderClient().tryToUpdateClient();
    }

    @Override
    public boolean isVideoCommunication() {
        return true;
    }

    @Override
    public String toString() {
        return "VIDEO" + "|" +getKey() + "|" + getOriginTerminal().getKey() + "|" +
                getDestinationTerminal().getKey() + "|" + getDuration()+ "|" + Math.round(getCost()) + "|" +
                (isOngoing() ? "ONGOING": "FINISHED");
    }
}

