package prr.terminals;

import prr.Network;
import prr.clients.Client;
import prr.communications.*;
import prr.communications.Communication;
import prr.exceptions.*;
import prr.notifications.Notification;
import prr.notifications.NotificationService;

import java.io.Serializable;
import java.util.*;

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable {

	private static final long serialVersionUID = 202208091753L;
    private String _key;
    private Client _client;
    private TerminalStatus _state;
    private TerminalStatus _previousState;
    private Terminal _connectedTerminal;
    private InteractiveCommunication _onGoingCommunication;
    private List<Communication> _realizedCommunications = new ArrayList<>();
    private List<Communication> _receivedCommunications = new ArrayList<>();
    private Map<String, Terminal>  _friends = new TreeMap<>();
    private List<Double> _paymentsMade = new ArrayList<>();
    private List<Double> _debts = new ArrayList<>();
    private NotificationService _notificationService = new NotificationService();


    public Terminal(String key, Client client, String state) {
        _key = key;
        _client = client;
        switch (state) {
            case "ON" -> _state = new IdleState(this);
            case "OFF" -> _state = new OffState(this);
            case "SILENCE" -> _state = new SilenceState(this);
        }
    }

    public void setState(TerminalStatus state) {
        _state = state;
    }
    public void setOnGoingCommunication(InteractiveCommunication onGoingComm) {
        _onGoingCommunication = onGoingComm;
    }
    public void savePreviousState() {
        _previousState = _state;
    }
    public void setConnectedTerminal(Terminal connectedTerminal) {
        _connectedTerminal = connectedTerminal;
    }

    public String getKey() {
        return _key;
    }
    public Client getClient() {
        return _client;
    }
    public TerminalStatus getTerminalStatus() {
        return _state;
    }
    public Terminal getConnectedTerminal() {
        return _connectedTerminal;
    }
    public List<Communication> getRealizedCommunications() {
        return _realizedCommunications;
    }
    public List<Communication> getReceivedCommunications() {
        return _receivedCommunications;
    }
    public List<Double> getPaymentsMade() {
        return _paymentsMade;
    }
    public List<Double> getDebts() {
        return _debts;
    }
    public NotificationService getNotificationService() {
        return _notificationService;
    }

    public List<Terminal> getFriendsList() {
        return new ArrayList<>(_friends.values());
    }

    public long getTotalPayments() {
        double total = 0;

        for (double payment: _paymentsMade)
            total += payment;

        return Math.round(total);
    }
    public long getTotalDebt() {
        double total = 0;

        for (double debt:_debts)
            total += debt;

        return Math.round(total);
    }
    public long getTerminalBalance() {
        return getTotalPayments() - getTotalDebt();
    }

    public Communication getOngoingComm() throws NoOnGoingCommunicationException {
        if (_onGoingCommunication != null && _onGoingCommunication.isOngoing()) {
            return _onGoingCommunication;
        } else
            throw new NoOnGoingCommunicationException();
    }

    public long getInteractiveCommunicationCost(int duration) {
        _onGoingCommunication.setDuration(duration);
        _onGoingCommunication.calculateAndRegisterCost();

        return Math.round(_onGoingCommunication.getCost());
    }

    public Communication getValidToPayCommunication(int commKey) throws InvalidCommunicationException {
        for (Communication comm : _realizedCommunications)
            if (comm.getKey() == commKey) {
                if (comm.hasBeenPaid() || comm.isOngoing())
                    throw new InvalidCommunicationException();
                return comm;
            }
        // if the communication was not made by the current terminal
        throw new InvalidCommunicationException();
    }

    public void turnOn() throws TerminalAlreadyOnException {
        _state.on();
    }
    public void turnOff() throws TerminalAlreadyOffException {
        _state.off();
    }
    public void turnSilent() throws TerminalAlreadySilentException {
        _state.silence();
    }
    public void turnBusy() {
        _state.busy();
    }

    public void addFriend(String terminalKey, Network network) throws IllegalTerminalKeyException,
            UnknownTerminalException
    {
        network.validateTerminal(terminalKey);

        // A terminal cannot be friends with himself
        if (!terminalKey.equals(_key))
            _friends.put(terminalKey, network.getTerminal(terminalKey));
    }

    public void addToRealizedCommunications(Communication comm) {
        _realizedCommunications.add(comm);
    }
    public void addToReceivedCommunications(Communication comm) {
        _receivedCommunications.add(comm);
    }
    public void addPayment(double payment) {
        _paymentsMade.add(payment);
    }
    public void addDebt(double debt) {
        _debts.add(debt);
    }

    public void removeDebt(double debt) {
        _debts.remove(debt);
    }
    public void removeFriend(String terminalKey) {
        _friends.remove(terminalKey);
    }

    public boolean canEndCurrentCommunication() {
        return _state.isBusy() && isOriginTerminal();
    }
    public boolean canStartCommunication() {
        return _state.isIdle() || _state.isSilence();
    }

    public boolean isOriginTerminal() {
        if (_onGoingCommunication.getOriginTerminal() == null)
            return false;

        else return _onGoingCommunication.getOriginTerminal().getKey().equals(_key);
    }

    public boolean isFriendsWith(Terminal terminal) {
        return _friends.containsKey(terminal.getKey());
    }

    public boolean isDisabled() {
        return _realizedCommunications.isEmpty() && _receivedCommunications.isEmpty();
    }

    public abstract boolean cannotStartVideoCommunication();

    public String allFriendsKeysToString() {
        StringBuilder allFriendsKeys = new StringBuilder().append("|");
        String prefix = "";

        for (Terminal terminalFriend : getFriendsList()) {
            allFriendsKeys.append(prefix);
            prefix = ", ";
            allFriendsKeys.append(terminalFriend.getKey());
        }
        return allFriendsKeys.toString();
    }

    public void tryToSubscribeClient(Client client) {
        if (client.hasNotificationsEnabled())
            _notificationService.subscribe(client);
    }

    public void confirmPossibilityToStartVideoCommunication(Terminal destinationTerminal)
            throws UnsupportedAtOriginException, UnsupportedAtDestinationException
    {
        if (this.cannotStartVideoCommunication())
            throw new UnsupportedAtOriginException();
        if (destinationTerminal.cannotStartVideoCommunication())
            throw new UnsupportedAtDestinationException();
    }

    public void confirmDestinationAvailabilityToCommunicate(Terminal originTerminal) throws DestinationIsBusyException,
            DestinationIsOffException, DestinationIsSilentException
    {
        // if the terminal is trying to communicate with himself
        if (originTerminal.getKey().equals(_key)) {
            tryToSubscribeClient(originTerminal.getClient());
            throw new DestinationIsBusyException();
        }
        _state.destinationAvailability(originTerminal.getClient());
    }

    public void confirmDestinationAvailabilityToText(Client client) throws DestinationIsOffException {
        if (_state.isOff()) {
            tryToSubscribeClient(client);
            throw new DestinationIsOffException();
        }
    }

    public void sendTextCommunication(String terminalKey, String text, Network network)
            throws DestinationIsOffException, IllegalTerminalKeyException, UnknownTerminalException
    {
        Terminal destinationTerminal = network.getTerminal(terminalKey);
        int id = network.getNewCommunicationKey();
        TextCommunication comm = new TextCommunication(id, this, destinationTerminal, text);

        destinationTerminal.confirmDestinationAvailabilityToText(this.getClient());
        comm.calculateAndRegisterCost();

        network.addCommunication(comm);
        this.addToRealizedCommunications(comm);
        destinationTerminal.addToReceivedCommunications(comm);
    }

    public void registerInteractiveCommunication(InteractiveCommunication comm, boolean isReceiver)
    {
        setOnGoingCommunication(comm);
        comm.start();
        savePreviousState();
        turnBusy();

        if (isReceiver)
            addToReceivedCommunications(comm);
        else
            addToRealizedCommunications(comm);
    }

    public void startInteractiveCommunication(String destinationTerminalKey, String communicationType, Network network)
            throws UnsupportedAtOriginException, UnsupportedAtDestinationException,
            DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException,
            IllegalTerminalKeyException, UnknownTerminalException
    {
        int id = network.getNewCommunicationKey();
        Terminal destinationTerminal = network.getTerminal(destinationTerminalKey);

        destinationTerminal.confirmDestinationAvailabilityToCommunicate(this);
        setConnectedTerminal(destinationTerminal);

        if (communicationType.equals("VIDEO")) {
            InteractiveCommunication comm = new VideoCommunication(id, this, destinationTerminal);
            confirmPossibilityToStartVideoCommunication(destinationTerminal);

            this.registerInteractiveCommunication(comm, true);
            destinationTerminal.registerInteractiveCommunication(comm, false);
            network.addCommunication(comm);

        } else if (communicationType.equals("VOICE")) {
            InteractiveCommunication comm = new VoiceCommunication(id, this, destinationTerminal);

            this.registerInteractiveCommunication(comm, true);
            destinationTerminal.registerInteractiveCommunication(comm, false);
            network.addCommunication(comm);
        }
    }

    public void endCommunication() {
        _onGoingCommunication.end();
        setOnGoingCommunication(new InteractiveCommunication());

        if (_previousState.isIdle())
            getNotificationService().notify(new Notification("B2I", this));
        setState(_previousState);
    }

    public void performPayment(int commKey) throws InvalidCommunicationException {
        Communication comm = getValidToPayCommunication(commKey);

       removeDebt(comm.getCost());
       addPayment(comm.getCost());

       if (_client.getBalance() > 500 && _client.isNormal())
           _client.updateToGold();
    }

    public abstract String toString();
}
