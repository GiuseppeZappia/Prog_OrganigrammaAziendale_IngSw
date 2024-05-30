package command;

import composite.OrganoGestione;
import composite.UnitaOrganizzativa;
import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import memento.PannelloDisegnoMemento;

public class RimuoviFiglioCommand implements Command{

    private OrganigrammaElement elementoDaElimin,padre;
    private PannelloDisegno pd;
    private PannelloDisegnoMemento memento;

    public RimuoviFiglioCommand(OrganigrammaElement padre,OrganigrammaElement ele, PannelloDisegno pd){
        this.padre=padre;
        this.elementoDaElimin=ele;
        this.pd=pd;
        this.memento=pd.creaMemento();
    }

    @Override
    public boolean doIt() {
        try {
            if(padre==null){
                OrganoGestione orgGest=(OrganoGestione) elementoDaElimin;
                orgGest.rimuoviTutto();
            }
            else if(padre instanceof OrganoGestione){
                padre.removeChild(elementoDaElimin);
            }
            else{
            UnitaOrganizzativa elemento=(UnitaOrganizzativa) elementoDaElimin;
            elemento.rimuoviTutti();//rimuovo tutti i suoi figli
            padre.removeChild(elemento);
            }
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean undoIt(){
        pd.ripristinaMemento(memento);
        return true;
    }
}