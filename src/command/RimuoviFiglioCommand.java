package command;

import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import composite.OrganigrammaElement;

public class RimuoviFiglioCommand implements Command{

    private OrganigrammaElement elem,padre;

    public RimuoviFiglioCommand(OrganigrammaElement padre,OrganigrammaElement elem) {
        this.padre=padre;
        this.elem=elem;
    }


    @Override
    public boolean doIt() {
        try {
            padre.removeChild(elem);
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    @Override
    public boolean undoIt() {
        try{
            padre.addChild(elem);
        }catch(SubjectSenzaListenerInAscoltoException |FiglioUnitaNonValidoException e){
            throw new RuntimeException(e);
        }
        return true;
    }
}
