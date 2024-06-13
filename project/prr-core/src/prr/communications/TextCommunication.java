package prr.communications;

import prr.terminals.Terminal;

public class TextCommunication extends Communication {
    private String _text;

    public TextCommunication(int id, Terminal originTerminal, Terminal destinationTerminal, String text) {
        super(id, originTerminal, destinationTerminal);
        _text = text;
    }

    public int getTextLength() {
        return _text.length();
    }

    @Override
    public void calculateAndRegisterCost() {
        double cost = getSenderClientTariffPlan().getTextCommunicationCost(this);

        setCost(cost);
        getOriginTerminal().addDebt(cost);
        getSenderClient().tryToUpdateClient();
    }

    @Override
    public boolean isTextCommunication() {
        return true;
    }
    @Override
    public boolean isVideoCommunication() {
        return false;
    }

    @Override
    public String toString() {
        return "TEXT" + "|" + getKey() + "|" + getOriginTerminal().getKey() + "|" +
                getDestinationTerminal().getKey() + "|" + getTextLength() + "|" + Math.round(getCost()) + "|" +
                (isOngoing() ? "ONGOING": "FINISHED");
    }
}
