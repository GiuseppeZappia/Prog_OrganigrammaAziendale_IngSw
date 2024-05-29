package command;

import java.io.Serializable;

public interface CommandHandler extends Serializable {
    public void handleCommand(Command command);
}
