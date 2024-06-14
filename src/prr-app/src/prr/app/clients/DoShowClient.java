package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.clients.Client;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			Form request = new Form();
			request.addStringField("clientKey", Prompt.key());
			request.parse();

			Client client = _receiver.getClient(request.stringField("clientKey"));
			_display.popup(client);
			_display.popup(client.getNotificationsToString());
			client.cleanNotifications();

		} catch (UnknownClientException e) {
			throw new UnknownClientKeyException(e.getKey());
		}
	}
}
