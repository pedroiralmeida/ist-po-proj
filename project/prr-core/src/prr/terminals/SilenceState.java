package prr.terminals;

import prr.clients.Client;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.notifications.Notification;

public class SilenceState extends TerminalStatus {
    public SilenceState(Terminal terminal) {
        super(terminal);
    }

    public void on() {
        getTerminal().getNotificationService().notify(new Notification("S2I", getTerminal()));
        getTerminal().setState(new IdleState(getTerminal()));
    }
    public void busy() {
        getTerminal().setState(new BusyState(getTerminal()));
    }
    public void off() {
        getTerminal().setState(new OffState(getTerminal()));
    }
    public void silence() throws TerminalAlreadySilentException {
        throw new TerminalAlreadySilentException();
    }

    public boolean isBusy() {
        return false;
    }
    public boolean isOff() {
        return false;
    }
    public boolean isIdle() {
        return false;
    }
    public boolean isSilence() {
        return true;
    }

    public void destinationAvailability(Client client) throws DestinationIsSilentException {
        getTerminal().tryToSubscribeClient(client);
        throw new DestinationIsSilentException();
    }

    @Override
    public String toString() {
        return "SILENCE";
    }
}
