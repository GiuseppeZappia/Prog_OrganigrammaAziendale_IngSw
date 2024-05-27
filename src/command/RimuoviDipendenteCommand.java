package command;

import command.FactoryDialog.CreateDialog;
import command.FactoryDialog.RimuoviDipendenteDialogFactory;
import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import exceptions.DipendenteNonPresenteNellUnitaException;
import gui.PannelloDisegno;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class RimuoviDipendenteCommand implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;
    private CreateDialog createDialog;

    public RimuoviDipendenteCommand(PannelloDisegno pd, OrganigrammaElement elem){
        this.pd = pd;
        this.elem = elem;
        this.createDialog=new RimuoviDipendenteDialogFactory();
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