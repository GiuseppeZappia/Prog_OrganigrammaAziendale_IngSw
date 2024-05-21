package Command;

import java.util.LinkedList;

public class HistoryCommandHandler implements CommandHandler {
    private int maxHistoryLength = 100;

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
    public void handleCommand(Command cmd) {//sta ricevendo un comando nuovo, i vecchi non mi fregano

        if (cmd.disegna()) {
            // restituisce true: può essere annullato
            addToHistory(cmd);
        } else {
            // restituisce false: non può essere annullato quindi è come se non avessi piu storia
            history.clear();
        }
        if (!redoList.isEmpty())
            redoList.clear();
    }

    public void redo() {
        if (redoList.size() > 0) {
            Command redoCmd = redoList.removeFirst();
            redoCmd.disegna();
            history.addFirst(redoCmd);

        }
    }

    public void undo() {
        if (history.size() > 0) {
            Command undoCmd = history.removeFirst();
            undoCmd.rimuovi();
            redoList.addFirst(undoCmd);
        }
    }

    private void addToHistory(Command cmd) {
        history.addFirst(cmd);
        if (history.size() > maxHistoryLength) {
            history.removeLast();
        }

    }
}
