package command;

public class RimuoviDipendenteCommand implements Command{
    @Override
    public boolean doIt() {
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
