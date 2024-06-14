package prr.terminals;

import prr.clients.Client;
import prr.exceptions.TerminalAlreadyOnException;

public class IdleState extends TerminalStatus {
    public IdleState(Terminal terminal) {
        super(terminal);
    }

    public void silence() {
        getTerminal().setState(new SilenceState(getTerminal()));
    }
    public void busy() {
        getTerminal().setState(new BusyState(getTerminal()));
    }
    public void off() {
        getTerminal().setState(new OffState(getTerminal()));
    }
    public void on() throws TerminalAlreadyOnException {
        throw new TerminalAlreadyOnException();
    }

    public boolean isBusy() {
        return false;
    }
    public boolean isOff() {
        return false;
    }
    public boolean isIdle() {
        return true;
    }
    public boolean isSilence() {
        return false;
    }

    // available (no exceptions thrown)
    public void destinationAvailability(Client client) {}

    @Override
    public String toString() {
        return "IDLE";
    }
}
