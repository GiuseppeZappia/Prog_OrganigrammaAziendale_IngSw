package command;

import command.CreationDialogImplementation.AggiungiRuoloDialog;
import command.CreationDialogImplementation.CreateDialog;
import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import javax.swing.*;

public class AggiungiRuoloCommand implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;
    private CreateDialog createDialog;

    public AggiungiRuoloCommand(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        this.createDialog=new AggiungiRuoloDialog();
    }

    @Override
    public boolean doIt() {
        JDialog finestra=createDialog.createDialog(pd,elem);//CHIAMO creatore dialog COSI SEPARO ED HO TUTTI I CLASSICI VANTAGGI
        //POSSO PROVARE DIALOG DA SOLO, NON DO RESPOSABILITA NON SUE AL COMMAND E RENDO TUTTO PIU LEGGIBILE
        finestra.setVisible(true);
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }


}