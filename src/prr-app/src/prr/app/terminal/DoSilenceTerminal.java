package prr.app.terminal;

import prr.Network;
import prr.exceptions.TerminalAlreadySilentException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Silence the terminal.
 */
class DoSilenceTerminal extends TerminalCommand {

	DoSilenceTerminal(Network context, Terminal terminal) {
		super(Label.MUTE_TERMINAL, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.turnSilent();

		} catch (TerminalAlreadySilentException e) {
			_display.popup(Message.alreadySilent());
		}
	}
}
