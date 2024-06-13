package prr.exceptions;

public class DuplicateTerminalException extends NetworkException {
    private static final long serialVersionUID = 202208091753L;
    private final String _key;

    public DuplicateTerminalException(String key) {
        _key = key;
    }
    public String getKey() {
        return _key;
    }

}
