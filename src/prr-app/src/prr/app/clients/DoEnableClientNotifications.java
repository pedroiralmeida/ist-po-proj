package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.NotificationsAlreadyEnabledException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		Form request = new Form();
		request.addStringField("clientKey", Prompt.key());
		request.parse();

		try {
			_receiver.enableClientNotifications(request.stringField("clientKey"));

		} catch (UnknownClientException e) {
			throw new UnknownClientKeyException(e.getKey());
		} catch (NotificationsAlreadyEnabledException e) {
			_display.popup(Message.clientNotificationsAlreadyEnabled());
		}

	}
}
