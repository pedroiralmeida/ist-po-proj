package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.IllegalTerminalKeyException;
import prr.exceptions.UnknownTerminalException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

	DoOpenMenuTerminalConsole(Network receiver) {
		super(Label.OPEN_MENU_TERMINAL, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			Form request = new Form();
			request.addStringField("terminalKey", Prompt.terminalKey());
			request.parse();
			Terminal terminal = _receiver.getTerminal(request.stringField("terminalKey"));

			(new prr.app.terminal.Menu(_receiver, terminal)).open();

		} catch (UnknownTerminalException e) {
			throw new UnknownTerminalKeyException(e.getKey());
		}
		catch (IllegalTerminalKeyException e) {
			throw new InvalidTerminalKeyException(e.getKey());
		}
	}
}
