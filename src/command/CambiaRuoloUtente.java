package command;

import command.CreationDialogImplementation.CambiaRuoloUtenteDialog;
import command.CreationDialogImplementation.CreateDialog;
import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import javax.swing.*;

public class CambiaRuoloUtente implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;
    private CreateDialog createDialog;

    public CambiaRuoloUtente(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        createDialog=new CambiaRuoloUtenteDialog();
    }

    @Override
    public boolean doIt() {
        JDialog finestraRuoliPrese= createDialog.createDialog(pd,elem);
        finestraRuoliPrese.setVisible(true);
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}