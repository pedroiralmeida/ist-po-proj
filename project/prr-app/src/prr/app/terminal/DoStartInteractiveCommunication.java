package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.*;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
	}

	@Override
	protected final void execute() throws CommandException {
		Form request = new Form();
		request.addStringField("destinationTerminalKey", Prompt.terminalKey());
		request.addOptionField("communicationType", Prompt.commType(), "VOICE", "VIDEO");
		request.parse();

		String destinationTerminalKey = request.stringField("destinationTerminalKey");
		String communicationType = request.stringField("communicationType");

		try {
			_receiver.startInteractiveCommunication(destinationTerminalKey, communicationType, _network);

		} catch (UnsupportedAtOriginException e) {
			_display.popup(Message.unsupportedAtOrigin(_receiver.getKey(), communicationType));
		} catch (UnsupportedAtDestinationException e) {
			_display.popup(Message.unsupportedAtDestination(_receiver.getKey(), communicationType));
		} catch (DestinationIsOffException e) {
			_display.popup(Message.destinationIsOff(destinationTerminalKey));
		} catch (DestinationIsBusyException e) {
			_display.popup(Message.destinationIsBusy(destinationTerminalKey));
		} catch (DestinationIsSilentException e) {
			_display.popup(Message.destinationIsSilent(destinationTerminalKey));
		} catch (IllegalTerminalKeyException e) {
			throw new InvalidTerminalKeyException(e.getKey());
		} catch (UnknownTerminalException e) {
			throw new UnknownTerminalKeyException(e.getKey());
		}
	}
}

