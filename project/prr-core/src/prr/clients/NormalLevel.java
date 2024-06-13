package prr.clients;

public class NormalLevel extends ClientLevel {

    public NormalLevel(Client client) {
        super(client);
    }

    public boolean isNormal() {
        return true;
    }
    public boolean isGold() {
        return false;
    }
    public boolean isPlatinum() {
        return false;
    }

    public String toString() {
        return "NORMAL";
    }
}
