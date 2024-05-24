package Command;

import Composite.OrganigrammaElement;
import Exceptions.FiglioNonPresenteInQuestaUnitaException;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import GUI.PannelloDisegno;
import Mediator.ChangeManagerMediator;
import Composite.UnitaOrganizzativa;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class AggiungiFiglioUnitaCommand implements Command {

    private OrganigrammaElement elem,padre;

    public AggiungiFiglioUnitaCommand(OrganigrammaElement elem,OrganigrammaElement padre){
        this.elem = elem;
        this.padre = padre;
    }

    @Override
    public boolean disegna() {
        try {//perche nella scelta della listbox da spazio vuoto che non fa fare selezione ma se scelto da null
            padre.addChild(elem);
        } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    @Override
    public boolean rimuovi()  {
        try {
            padre.removeChild(elem);
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
