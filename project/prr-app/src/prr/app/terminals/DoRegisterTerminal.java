package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.DuplicateTerminalException;
import prr.exceptions.IllegalTerminalKeyException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			Form request = new Form();
			request.addStringField("terminalKey", Prompt.terminalKey());
			request.addOptionField("terminalType", Prompt.terminalType(), "BASIC", "FANCY");
			request.addStringField("clientKey", Prompt.clientKey());
			request.parse();

			_receiver.registerTerminal(request.stringField("terminalType"),
					request.stringField("terminalKey"),
					request.stringField("clientKey"), "ON");

		} catch (IllegalTerminalKeyException e) {
			throw new InvalidTerminalKeyException(e.getKey());
		} catch (DuplicateTerminalException e) {
			throw new DuplicateTerminalKeyException(e.getKey());
		} catch (UnknownClientException e) {
			throw new UnknownClientKeyException(e.getKey());
		}
	}
}
