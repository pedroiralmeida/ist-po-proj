package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.NotificationsAlreadyDisabledException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

	DoDisableClientNotifications(Network receiver) {
		super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		Form request = new Form();
		request.addStringField("clientKey", Prompt.key());
		request.parse();

		try {
			_receiver.disableClientNotifications(request.stringField("clientKey"));

		} catch (UnknownClientException e) {
			throw new UnknownClientKeyException(e.getKey());
		} catch (NotificationsAlreadyDisabledException e) {
			_display.popup(Message.clientNotificationsAlreadyDisabled());
		}
	}
}
