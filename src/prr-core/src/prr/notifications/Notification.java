package prr.notifications;

import prr.terminals.Terminal;

import java.io.Serializable;

public class Notification implements Serializable {

    private static final long serialVersionUID = 202208091753L;
    private Terminal _terminal;
    private String _notificationType;

    public Notification(String notificationType, Terminal terminal) {
        _notificationType = notificationType;
        _terminal = terminal;
    }

    public String toString() {
        return _notificationType + "|" + _terminal.getKey();
    }
}
