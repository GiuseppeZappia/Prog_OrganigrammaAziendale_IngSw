package command;

import command.FactoryDialog.AggiungiRuoloDialogFactory;
import command.FactoryDialog.CreateDialog;
import composite.OrganigrammaElement;
import composite.utilities.Ruolo;
import exceptions.RuoloGiaPresenteNellUnitaException;
import gui.PannelloDisegno;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.LinkedList;

public class AggiungiRuoloCommand implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;
    private CreateDialog createDialog;

    public AggiungiRuoloCommand(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        this.createDialog=new AggiungiRuoloDialogFactory();
    }

    @Override
    public boolean doIt() {
        JDialog finestra=createDialog.createDialog(pd,elem);//CHIAMO FACTORY COSI SEPARO ED HO TUTTI I CLASSICI VANTAGGI
        //POSSO PROVARE DIALOG DA SOLO, NON DO RESPOSABILITA NON SUE AL COMMAND E RENDO TUTTO PIU LEGGIBILE
        finestra.setVisible(true);
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }


}