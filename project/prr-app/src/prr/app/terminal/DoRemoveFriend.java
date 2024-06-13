package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
		Form request = new Form();
		request.addStringField("terminalKey", Prompt.terminalKey());
		request.parse();

		_receiver.removeFriend(request.stringField("terminalKey"));
	}
}
