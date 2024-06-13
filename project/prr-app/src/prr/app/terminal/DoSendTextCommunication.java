package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.IllegalTerminalKeyException;
import prr.exceptions.UnknownTerminalException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {
        DoSendTextCommunication(Network context, Terminal terminal) {
                super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
        }

        @Override
        protected final void execute() throws CommandException {
           Form request = new Form();

           request.addStringField("terminalKey", Prompt.terminalKey());
           request.addStringField("text", Prompt.textMessage());
           request.parse();

           String destinationTerminalKey = request.stringField("terminalKey");
           String text = request.stringField("text");

           try {
                _receiver.sendTextCommunication(destinationTerminalKey, text, _network);

           } catch (DestinationIsOffException e) {
                _display.popup(Message.destinationIsOff(destinationTerminalKey));
           } catch (IllegalTerminalKeyException e) {
                throw new InvalidTerminalKeyException(e.getKey());
           } catch (UnknownTerminalException e) {
               throw new UnknownTerminalKeyException(e.getKey());
           }
        }
}
