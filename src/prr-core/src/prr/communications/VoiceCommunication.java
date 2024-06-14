package prr.communications;

import prr.terminals.Terminal;

public class VoiceCommunication extends InteractiveCommunication {

    public VoiceCommunication(int id, Terminal originTerminal, Terminal destinationTerminal) {
        super(id, originTerminal, destinationTerminal);
    }

    @Override
    public void calculateAndRegisterCost() {
        double cost = getSenderClientTariffPlan().getVoiceCommunicationCost(this);

        setCost(cost);
        getOriginTerminal().addDebt(cost);
        getSenderClient().tryToUpdateClient();
    }

    @Override
    public boolean isVideoCommunication() {
        return false;
    }

    @Override
    public String toString() {
        return "VOICE" + "|" + getKey() + "|" + getOriginTerminal().getKey() + "|" +
                getDestinationTerminal().getKey() + "|" + getDuration()+ "|" + Math.round(getCost()) + "|" +
                (isOngoing() ? "ONGOING": "FINISHED");
    }
}
