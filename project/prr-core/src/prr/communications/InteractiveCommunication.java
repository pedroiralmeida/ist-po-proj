package prr.communications;

import prr.terminals.Terminal;

public class InteractiveCommunication extends Communication {
    private int _duration;

    public InteractiveCommunication(int id, Terminal originTerminal, Terminal destinationTerminal) {
        super(id, originTerminal, destinationTerminal);
    }

    public InteractiveCommunication() {}

    public void setDuration(int duration) {
        _duration = duration;
    }

    public int getDuration() {
        return _duration;
    }

    @Override
    public void calculateAndRegisterCost() {}

    @Override
    public boolean isTextCommunication() {
        return false;
    }
    @Override
    public boolean isVideoCommunication() {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
