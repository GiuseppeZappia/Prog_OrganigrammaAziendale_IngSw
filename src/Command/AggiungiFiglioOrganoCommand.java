package Command;

import Composite.OrganigrammaElement;
import Composite.OrganoGestione;
import Composite.UnitaOrganizzativa;
import Exceptions.FiglioNonPresenteInQuestaUnitaException;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import GUI.PannelloDisegno;
import Mediator.ChangeManagerMediator;

import javax.swing.*;
import java.util.LinkedList;

public class AggiungiFiglioOrganoCommand implements Command {

    private ChangeManagerMediator mediator;
    private OrganigrammaElement elem;
    private OrganigrammaElement padre;

    public AggiungiFiglioOrganoCommand( ChangeManagerMediator m,OrganigrammaElement elem, OrganigrammaElement padre) {
        this.mediator=m;
        this.elem=elem;
        this.padre=padre;
    }


//controlla bene return per undo e redo
    @Override
    public boolean disegna() {
        try {
            padre.addChild(elem);//observer dentro sto metodo
        } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean rimuovi() {
        try {
            padre.removeChild(elem);
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
//        pd.rimuoviUnita(elem);//questo non chiama stessa funzione perche
        return true;
    }



}
