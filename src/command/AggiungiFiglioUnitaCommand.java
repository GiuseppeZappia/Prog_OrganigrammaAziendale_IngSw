package command;

import composite.OrganigrammaElement;
import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;

public class AggiungiFiglioUnitaCommand implements Command {

    private OrganigrammaElement elem,padre;

    public AggiungiFiglioUnitaCommand(OrganigrammaElement elem,OrganigrammaElement padre){
        this.elem = elem;
        this.padre = padre;
    }

    @Override
    public boolean doIt() {
        try {//perche nella scelta della listbox da spazio vuoto che non fa fare selezione ma se scelto da null
            padre.addChild(elem);
        } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    @Override
    public boolean undoIt()  {
        try {
            padre.removeChild(elem);
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
