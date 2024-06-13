package prr.app.terminal;

import prr.Network;
import prr.exceptions.InvalidCommunicationException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

	DoPerformPayment(Network context, Terminal terminal) {
		super(Label.PERFORM_PAYMENT, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
		Form request = new Form();
		request.addIntegerField("communicationKey", Prompt.commKey());
		request.parse();

		try {
			_receiver.performPayment(request.integerField("communicationKey"));

		} catch (InvalidCommunicationException e) {
			_display.popup(Message.invalidCommunication());
		}
	}
}
