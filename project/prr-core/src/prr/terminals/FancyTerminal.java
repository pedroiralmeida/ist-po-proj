package prr.terminals;

import prr.clients.Client;

public class FancyTerminal extends BasicTerminal {
    public FancyTerminal(String key, Client client, String state) {
        super(key, client, state);
    }

    @Override
    public boolean cannotStartVideoCommunication() {
        return false;
    }

    @Override
    public String toString() {
        return "FANCY" + "|" + getKey() + "|" + getClient().getKey() + "|" + getTerminalStatus() + "|" +
                getTotalPayments() + "|" + getTotalDebt() +
                (getFriendsList().isEmpty() ? "" : allFriendsKeysToString());
    }
}
