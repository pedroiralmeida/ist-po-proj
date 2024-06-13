package prr.exceptions;

public class UnknownClientException extends NetworkException {

    private static final long serialVersionUID = 202208091753L;
    private final String _key;

    public UnknownClientException (String key) {
        _key = key;
    }
    public String getKey() {
        return _key;
    }
}

