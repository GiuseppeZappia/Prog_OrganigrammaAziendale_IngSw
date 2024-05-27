package command;

import command.FactoryDialog.CreateDialog;
import command.FactoryDialog.RimuoviRuoloDialogFactory;
import composite.OrganigrammaElement;
import composite.utilities.Ruolo;
import exceptions.RuoloNonPresenteNellUnitaException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import gui.PannelloDisegno;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class RimuoviRuoloCommand implements Command{
    private OrganigrammaElement elem;
    private PannelloDisegno pd;
    private CreateDialog createDialog;

    public RimuoviRuoloCommand(PannelloDisegno pd,OrganigrammaElement elem) {
        this.elem = elem;
        this.pd = pd;
        this.createDialog=new RimuoviRuoloDialogFactory();
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
