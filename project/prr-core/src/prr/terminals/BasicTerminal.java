package prr.terminals;

import prr.clients.Client;

public class BasicTerminal extends Terminal {
    public BasicTerminal(String terminalKey, Client client, String state) {
        super(terminalKey, client, state);
    }

    @Override
    public boolean cannotStartVideoCommunication() {
        return true;
    }

    public String toString() {
        return "BASIC" + "|" + getKey() + "|" + getClient().getKey() + "|" + getTerminalStatus() + "|" +
                getTotalPayments() + "|" + getTotalDebt() +
                (getFriendsList().isEmpty() ? "" : allFriendsKeysToString());
    }
}
