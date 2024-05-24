package Command;

import Exceptions.FiglioNonPresenteInQuestaUnitaException;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import GUI.PannelloDisegno;
import Mediator.ChangeManagerMediator;
import Composite.OrganoGestione;
import Composite.UnitaOrganizzativa;
import Composite.OrganigrammaElement;
import javax.swing.*;
import java.awt.*;

public class RimuoviFiglioCommand implements Command{

    private OrganigrammaElement elem,padre;

    public RimuoviFiglioCommand(OrganigrammaElement padre,OrganigrammaElement elem) {
        this.padre=padre;
        this.elem=elem;
    }


    @Override
    public boolean disegna() {
        try {
            padre.removeChild(elem);
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    @Override
    public boolean rimuovi() {
        try{
            padre.addChild(elem);
        }catch(SubjectSenzaListenerInAscoltoException |FiglioUnitaNonValidoException e){
            throw new RuntimeException(e);
        }
        return true;
    }
}
