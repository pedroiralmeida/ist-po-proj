package prr.clients;

public class PlatinumLevel extends ClientLevel {

    public PlatinumLevel(Client client) {
        super(client);
    }

    public boolean isNormal() {
        return false;
    }
    public boolean isGold() {
        return false;
    }
    public boolean isPlatinum() {
        return true;
    }

    public String toString() {
        return "PLATINUM";
    }
}
