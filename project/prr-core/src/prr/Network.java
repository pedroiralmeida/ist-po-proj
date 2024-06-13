package prr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.io.IOException;
import java.util.*;

import prr.clients.Client;
import prr.communications.Communication;
import prr.exceptions.*;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.terminals.SortByTerminalID;
import prr.terminals.Terminal;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {
	/**
	 * Serial number for serialization.
	 */
	private static final long serialVersionUID = 202208091753L;
	/**
	 * If network object has been changed.
	 */
	private boolean _changed = false;
	/**
	 * Clients
	 */
	private Map<String, Client> _clients = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	/**
	 * Terminals
	 */
	private Map<String, Terminal> _terminals = new TreeMap<>();
	/**
	 * Communications
	 */
	private List<Communication> _communications = new ArrayList<>();

	/**
	 * @param changed
	 */
	public void setChanged(boolean changed) {
		_changed = changed;
	}

	/**
	 * @param filename
	 * @throws ImportFileException
	 * @throws IOException
	 */
	public void importFile(String filename) throws ImportFileException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\|");
				try {
					registerEntry(fields);
				} catch (UnrecognizedEntryException | DuplicateTerminalException | UnknownClientException |
						 DuplicateClientException | IllegalTerminalKeyException | UnknownTerminalException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			throw new ImportFileException(filename);
		}
	}

	/**
	 * Registers a client or a terminal in the network, or adds some given friends to a terminal.
	 *
	 * @param fields
	 * @throws UnrecognizedEntryException
	 * @throws DuplicateTerminalException
	 * @throws DuplicateClientException
	 * @throws UnknownClientException
	 * @throws IllegalTerminalKeyException
	 * @throws UnknownTerminalException
	 */
	public void registerEntry(String... fields) throws UnrecognizedEntryException, DuplicateTerminalException,
			DuplicateClientException, UnknownClientException, IllegalTerminalKeyException, UnknownTerminalException {
		switch (fields[0]) {
			case "CLIENT" -> registerClient(fields);
			case "BASIC", "FANCY" -> registerTerminal(fields);
			case "FRIENDS" -> addFriendsToTerminal(fields);
			default -> throw new UnrecognizedEntryException(fields[0]);
		}
	}

	/**
	 * Adds a given client described by the user to the clients map.
	 *
	 * @param fields
	 * @throws UnrecognizedEntryException
	 * @throws DuplicateClientException
	 */
	public void registerClient(String... fields) throws UnrecognizedEntryException, DuplicateClientException {
		if (!fields[0].equals("CLIENT"))
			throw new UnrecognizedEntryException(fields[0]);

		String clientKey = fields[1];
		String clientName = fields[2];
		int clientTaxID = Integer.parseInt(fields[3]);

		Client client = new Client(clientKey, clientName, clientTaxID);
		addClient(clientKey, client);
	}

	/**
	 * Adds a given terminal described by the user to the client with the given client key.
	 *
	 * @param fields
	 * @throws DuplicateTerminalException
	 * @throws UnknownClientException
	 * @throws IllegalTerminalKeyException
	 */
	public void registerTerminal(String... fields) throws DuplicateTerminalException, UnknownClientException,
			IllegalTerminalKeyException {
		String terminalType = fields[0];
		String terminalKey = fields[1];
		String clientKey = fields[2];
		String TerminalStatus = fields[3];

		validateTerminal(terminalKey, clientKey);
		Client client = _clients.get(clientKey);

		Terminal terminal = switch (terminalType) {
			case "BASIC" -> new BasicTerminal(terminalKey, client, TerminalStatus);
			case "FANCY" -> new FancyTerminal(terminalKey, client, TerminalStatus);
			// default case is unreachable
			default -> null;
		};
		client.addTerminal(terminal);
		_terminals.put(terminalKey, terminal);
		setChanged(true);
	}

	/**
	 * Adds the given terminal keys as friends of the terminal identified with the first given terminal key.
	 *
	 * @param fields
	 * @throws IllegalTerminalKeyException
	 * @throws UnknownTerminalException
	 */
	public void addFriendsToTerminal(String... fields) throws IllegalTerminalKeyException, UnknownTerminalException {
		String toAddTerminalKey = fields[1];
		Terminal toAddTerminal = _terminals.get(toAddTerminalKey);
		String line = fields[2];
		String[] friends = line.split(",");

		validateTerminal(toAddTerminalKey);

		for (String terminalFriendKey : friends) {
			toAddTerminal.addFriend(terminalFriendKey, this);
		}
	}

	/**
	 * @param clientKey
	 * @param client
	 * @throws DuplicateClientException
	 */
	public void addClient(String clientKey, Client client) throws DuplicateClientException {
		if (isClientRegistered(clientKey)) {
			throw new DuplicateClientException(clientKey);
		} else {
			_clients.put(clientKey, client);
			setChanged(true);
		}
	}

	public void addCommunication(Communication communication) {
		_communications.add(communication);
	}

	/**
	 * @return changed.
	 */
	public boolean hasChanged() {
		return _changed;
	}

	public List<Communication> getCommunications() {
		return _communications;
	}

	public int getNewCommunicationKey() {
		return _communications.size() + 1;
	}

	public List<Communication> getCommunicationsFromClient(String clientKey) throws UnknownClientException {
		return getClient(clientKey).getRealizedCommunications();
	}

	public List<Communication> getCommunicationsToClient(String clientKey) throws UnknownClientException {
		return getClient(clientKey).getReceivedCommunications();
	}

	/**
	 * Gets the client with the given id.
	 *
	 * @param clientKey
	 * @return the client identified by the key.
	 * @throws UnknownClientException
	 */
	public Client getClient(String clientKey) throws UnknownClientException {
		if (isClientRegistered(clientKey))
			return _clients.get(clientKey);
		else
			throw new UnknownClientException(clientKey);
	}

	/**
	 * @return an array list with all the registered clients.
	 */
	public List<Client> getAllClientsList() {
		return new ArrayList<>(_clients.values());
	}

	public List<Client> getClientsWithoutDebts() {
		List<Client> clientsWithoutDebts = new ArrayList<>();

		for (Client client: getAllClientsList())
			if (client.getTotalDebt() == 0)
				clientsWithoutDebts.add(client);

		return clientsWithoutDebts;
	}

	public List<Client> getClientsWithDebts() {
		List<Client> clientsWithDebts = new ArrayList<>();

		for (Client client: getAllClientsList())
			if (client.getTotalDebt() != 0)
				clientsWithDebts.add(client);

		return clientsWithDebts;
	}

	public long getTotalOfGlobalPayments() {
		double total = 0;

		for (Client client : getAllClientsList())
			total += client.getTotalPayments();

		return Math.round(total);
	}

	public long getTotalOfGlobalDebts() {
		double total = 0;

		for (Client client : getAllClientsList())
			total += client.getTotalDebt();

		return Math.round(total);
	}

	/**
	 * @param terminalKey
	 * @return the terminal identified by the key.
	 * @throws IllegalTerminalKeyException
	 * @throws UnknownTerminalException
	 */
	public Terminal getTerminal(String terminalKey) throws IllegalTerminalKeyException, UnknownTerminalException {
		validateTerminal(terminalKey);
		return _terminals.get(terminalKey);
	}
	/**
	 * @return an array list with all registered terminals.
	 */
	public List<Terminal> getAllTerminalsList() {
		return new ArrayList<>(_terminals.values());
	}

	/**
	 * @return an array list with all the unused terminals.
	 */
	public List<Terminal> getUnusedTerminals() {
		List<Terminal> unusedTerminals = new ArrayList<>();

		for (Terminal terminal : getAllTerminalsList())
			if (terminal.isDisabled())
				unusedTerminals.add(terminal);

		unusedTerminals.sort(new SortByTerminalID());
		return unusedTerminals;
	}

	public List<Terminal> getTerminalsWithPositiveBalance() {
		List<Terminal> terminalsWithPositiveBalance = new ArrayList<>();

		for (Terminal terminal: getAllTerminalsList())
			if (terminal.getTerminalBalance() > 0)
				terminalsWithPositiveBalance.add(terminal);

		return terminalsWithPositiveBalance;
	}

	/**
	 * @param terminalKey
	 * @return if there is a registered terminal with the given key.
	 */
	public boolean isTerminalRegistered(String terminalKey) {
		return _terminals.containsKey(terminalKey);
	}

	/**
	 * @param clientKey
	 * @return if there is a registered client with the given key.
	 */
	public boolean isClientRegistered(String clientKey) {
		return _clients.containsKey(clientKey);
	}

	/**
	 * @param terminalKey
	 * @return true if the given terminal key is not numeric.
	 */
	public boolean isNotNumeric(String terminalKey) {
		try {
			Integer.parseInt(terminalKey);
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

	/**
	 * Validates if a terminal is registered and if his key is legal.
	 *
	 * @param terminalKey
	 * @throws UnknownTerminalException
	 * @throws IllegalTerminalKeyException
	 */
	public void validateTerminal(String terminalKey) throws UnknownTerminalException, IllegalTerminalKeyException {
		if (!isTerminalRegistered(terminalKey))
			throw new UnknownTerminalException(terminalKey);
		if (terminalKey.length() != 6 || isNotNumeric(terminalKey)) {
			throw new IllegalTerminalKeyException(terminalKey);
		}
	}

	/**
	 * Validates if a terminal already exists, if the client associated with terminal exists and if
	 * the terminal's key is legal.
	 *
	 * @param terminalKey
	 * @param clientKey
	 * @throws DuplicateTerminalException
	 * @throws UnknownClientException
	 * @throws IllegalTerminalKeyException
	 */
	public void validateTerminal(String terminalKey, String clientKey)
			throws DuplicateTerminalException, UnknownClientException, IllegalTerminalKeyException {
		if (terminalKey.length() != 6 || isNotNumeric(terminalKey))
			throw new IllegalTerminalKeyException(terminalKey);
		if (!isClientRegistered(clientKey))
			throw new UnknownClientException(clientKey);
		if (isTerminalRegistered(terminalKey))
			throw new DuplicateTerminalException(terminalKey);
	}

	public void enableClientNotifications(String clientKey)
			throws UnknownClientException, NotificationsAlreadyEnabledException
	{
		Client client = getClient(clientKey);

		if (client.hasNotificationsEnabled())
			throw new NotificationsAlreadyEnabledException();

		client.setNotificationsActive(true);
	}

	public void disableClientNotifications(String clientKey)
			throws UnknownClientException, NotificationsAlreadyDisabledException
	{
		Client client = getClient(clientKey);

		if (!client.hasNotificationsEnabled())
			throw new NotificationsAlreadyDisabledException();

		client.setNotificationsActive(false);
	}
}