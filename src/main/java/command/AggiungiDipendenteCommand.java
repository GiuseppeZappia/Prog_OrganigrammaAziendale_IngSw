package command;

import command.CreationDialogImplementation.AggiungiDipendenteDialog;
import command.CreationDialogImplementation.CreateDialog;
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
        this.createDialog=new AggiungiDipendenteDialog();
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
