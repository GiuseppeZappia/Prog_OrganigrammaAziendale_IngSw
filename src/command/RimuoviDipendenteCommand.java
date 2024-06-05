package command;

import command.CreationDialogImplementation.CreateDialog;
import command.CreationDialogImplementation.RimuoviDipendenteDialog;
import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import javax.swing.*;

public class RimuoviDipendenteCommand implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;
    private CreateDialog createDialog;

    public RimuoviDipendenteCommand(PannelloDisegno pd, OrganigrammaElement elem){
        this.pd = pd;
        this.elem = elem;
        this.createDialog=new RimuoviDipendenteDialog();
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