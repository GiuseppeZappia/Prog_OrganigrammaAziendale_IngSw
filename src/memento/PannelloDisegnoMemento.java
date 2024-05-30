package memento;

import composite.OrganigrammaElement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

//QUESTO È L'UNICO MEMENTO CHE MI SERVE, INFATTI DEVE TENERE TRACCIA DELLO STATO DI TUTTI I FIGLI, MENTRE PER LE UNITAORGANIZZATIVE
//NON NE HO BISOGNO PERCHE BASTA SEMPLICEMENTE CHE SI RIAGGIUNGANO COME CHILD DEL PADRE NELLA REDO.

public class PannelloDisegnoMemento implements Serializable {
    private LinkedHashMap<OrganigrammaElement,LinkedList<OrganigrammaElement>> figliPresenti=new LinkedHashMap<>();

    public PannelloDisegnoMemento(LinkedHashMap<OrganigrammaElement,LinkedList<OrganigrammaElement>> figli) {
        this.figliPresenti=figli;
    }

    public LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> getFigli() {
        return figliPresenti;
    }




//    private LinkedList<OrganigrammaElement> figliPresenti=new LinkedList<>();
//    private Object originator;


//    public PannelloDisegnoMemento(LinkedList<OrganigrammaElement> list, Object originator) {
//        figliPresenti=list;
//        this.originator=originator;
//    }
//
//    public Object getOriginator() {
//        return originator;
//    }

//    public LinkedList<OrganigrammaElement> getFigliPresenti() {
//        return figliPresenti;
//    }
}
