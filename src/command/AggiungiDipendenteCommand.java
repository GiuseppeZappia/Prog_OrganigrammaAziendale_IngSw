package command;

import command.FactoryDialog.AggiungiDipendenteDialogFactory;
import command.FactoryDialog.CreateDialog;
import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import javax.swing.*;

public class AggiungiDipendenteCommand implements Command {
    private PannelloDisegno pd;
    private OrganigrammaElement elem;
    private CreateDialog createDialog;

    public AggiungiDipendenteCommand(PannelloDisegno pd,OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        this.createDialog=new AggiungiDipendenteDialogFactory();
    }

    @Override
    public boolean doIt() {
        JDialog finestra=createDialog.createDialog(pd,elem);
        finestra.setVisible(true);
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }


}
