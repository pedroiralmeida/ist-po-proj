package prr.terminals;

import prr.clients.Client;
import prr.exceptions.DestinationIsBusyException;

public class BusyState extends TerminalStatus {
    public BusyState(Terminal terminal) {
        super(terminal);
    }

    public void on() {}
    public void silence() {}
    public void off() {}
    public void busy() {}

    public boolean isBusy() {
        return true;
    }
    public boolean isOff() {
        return false;
    }
    public boolean isIdle() {
        return false;
    }
    public boolean isSilence() {
        return false;
    }

    public void destinationAvailability(Client client) throws DestinationIsBusyException {
        getTerminal().tryToSubscribeClient(client);
        throw new DestinationIsBusyException();
    }

    @Override
    public String toString() {
        return "BUSY";
    }
}
