package command;

import java.io.IOException;
import java.util.LinkedList;

public class HistoryCommandHandler implements CommandHandler {
    private static final long serialVersionUID = 1L;
    private final int maxHistoryLength;
    private final LinkedList<Command> history = new LinkedList<>();
    private final LinkedList<Command> redoList = new LinkedList<>();

    public HistoryCommandHandler() {
        this(100);
    }

    public HistoryCommandHandler(int maxHistoryLength) {
        if (maxHistoryLength < 0)
            throw new IllegalArgumentException();
        this.maxHistoryLength = maxHistoryLength;
    }

    @Override
    public void handleCommand(Command cmd) {//sta ricevendo un comando nuovo
        try {
            if (cmd.doIt()) {
                // restituisce true: può essere annullato
                addToHistory(cmd);
            } else {
                // restituisce false: non può essere annullato quindi è come se non avessi piu storia
                history.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!redoList.isEmpty())
            redoList.clear();
    }

    public void redo() {
        if (!redoList.isEmpty()) {
            Command redoCmd = redoList.removeFirst();
            try {
                redoCmd.doIt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            history.addFirst(redoCmd);

        }
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command undoCmd = history.removeFirst();
            undoCmd.undoIt();
            redoList.addFirst(undoCmd);
        }
    }

    private void addToHistory(Command cmd) {
        history.addFirst(cmd);
        if (history.size() > maxHistoryLength) {
            history.removeLast();
        }
    }

    //AGGIUNTI SOLO PER TESTING, NON VERRANNO RILASCIATI!!
    public LinkedList<Command> getHistory() {
        return history;
    }

    public LinkedList<Command> getRedoList() {
        return redoList;
    }
}
