package memento;

import composite.OrganigrammaElement;

import java.util.HashMap;
import java.util.LinkedList;

//QUESTO È L'UNICO MEMENTO CHE MI SERVE, INFATTI DEVE TENERE TRACCIA DELLO STATO DI TUTTI I FIGLI, MENTRE PER LE UNITAORGANIZZATIVE
//NON NE HO BISOGNO PERCHE BASTA SEMPLICEMENTE CHE SI RIAGGIUNGANO COME CHILD DEL PADRE NELLA REDO.



public class OrganoGestioneMemento {
    private HashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti=new HashMap<>();

    public OrganoGestioneMemento(HashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figli) {
        this.figliPresenti=figli;
    }

    public HashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> getFigli() {
        return figliPresenti;
    }

}
