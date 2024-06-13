package prr.terminals;

import prr.clients.Client;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.notifications.Notification;

public class OffState extends TerminalStatus {
    public OffState(Terminal terminal) {
        super(terminal);
    }

    public void on() {
        getTerminal().getNotificationService().notify(new Notification("O2I", getTerminal()));
        getTerminal().setState(new IdleState(getTerminal()));
    }
    public void silence() {
        getTerminal().getNotificationService().notify(new Notification("O2S", getTerminal()));
        getTerminal().setState(new SilenceState(getTerminal()));
    }
    public void off() throws TerminalAlreadyOffException {
        throw new TerminalAlreadyOffException();
    }
    public void busy() {}

    public boolean isBusy() {
        return false;
    }
    public boolean isOff() {
        return true;
    }
    public boolean isIdle() {
        return false;
    }
    public boolean isSilence() {
        return false;
    }

    public void destinationAvailability(Client client) throws DestinationIsOffException {
        getTerminal().tryToSubscribeClient(client);
        throw new DestinationIsOffException();
    }

    @Override
    public String toString() {
        return "OFF";
    }
}
