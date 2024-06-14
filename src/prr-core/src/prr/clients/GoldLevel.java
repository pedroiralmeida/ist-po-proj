package prr.clients;

public class GoldLevel extends ClientLevel {

    public GoldLevel(Client client) {
        super(client);
    }

    public boolean isNormal() {
        return false;
    }
    public boolean isGold() {
        return true;
    }
    public boolean isPlatinum() {
        return false;
    }

    public String toString() {
        return "GOLD";
    }
}
