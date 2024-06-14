package prr.app.lookups;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

	DoShowCommunicationsToClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			Form request = new Form();
			request.addStringField("clientKey", Prompt.clientKey());
			request.parse();

			_display.popup(_receiver.getCommunicationsToClient(request.stringField("clientKey")));

		} catch (UnknownClientException e) {
			throw new UnknownClientKeyException(e.getKey());
		}
	}
}
