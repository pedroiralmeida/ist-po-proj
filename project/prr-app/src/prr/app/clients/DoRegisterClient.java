package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import prr.exceptions.DuplicateClientException;
import prr.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		Form request = new Form();
		try {
			request.addStringField("clientKey", Prompt.key());
			request.addStringField("name", Prompt.name());
			request.addStringField("taxID", Prompt.taxId());
			request.parse();

			_receiver.registerClient("CLIENT", request.stringField("clientKey"),
					request.stringField("name"), request.stringField("taxID"));

		} catch (DuplicateClientException e) {
			throw new DuplicateClientKeyException(e.getKey());
		} catch (UnrecognizedEntryException e) {
			e.printStackTrace();
		}
	}
}