package prr.terminals;

import prr.clients.Client;
import prr.exceptions.*;

import java.io.Serializable;

public abstract class TerminalStatus implements Serializable {

    private static final long serialVersionUID = 202208091753L;
    private Terminal _terminal;

    public TerminalStatus(Terminal terminal) {
        _terminal = terminal;
    }

    public Terminal getTerminal() {
        return _terminal;
    }

    public abstract void on() throws TerminalAlreadyOnException;
    public abstract void silence() throws TerminalAlreadySilentException;
    public abstract void off() throws TerminalAlreadyOffException;
    public abstract void busy();

    public abstract boolean isBusy();
    public abstract boolean isOff();
    public abstract boolean isIdle();
    public abstract boolean isSilence();

    public abstract void destinationAvailability(Client client) throws DestinationIsSilentException,
            DestinationIsBusyException, DestinationIsOffException;

    public abstract String toString();
}
