package prr.terminals;

import java.util.Comparator;

public class SortByTerminalID implements Comparator<Terminal> {

    public int compare(Terminal terminal1, Terminal terminal2) {
        return terminal1.getKey().compareTo(terminal2.getKey());
    }
}
