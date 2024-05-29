package command;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Command {

    boolean doIt() throws IOException;

    boolean undoIt() ;
}
