package command;

import composite.OrganigrammaElement;
import composite.OrganoGestione;
import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import gui.PannelloDisegno;
import memento.OrganoGestioneMemento;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RimuoviTuttoCommand implements Command {
    private OrganoGestione orgGest;
    private PannelloDisegno pd;
    private OrganoGestioneMemento memento;

    public RimuoviTuttoCommand(PannelloDisegno pd, OrganoGestione orgGest) {
        this.pd=pd;
        this.orgGest = orgGest;
        this.memento=orgGest.creaMemento();
    }


    @Override
    public boolean doIt() {
        try {
            orgGest.rimuoviTutto();
        } catch (FiglioNonPresenteInQuestaUnitaException | SubjectSenzaListenerInAscoltoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean undoIt() {
        pd.aggiungiUnita(orgGest);//disegno organo
        orgGest.ripristinaDaMemento(memento);
        return true;
    }
}
