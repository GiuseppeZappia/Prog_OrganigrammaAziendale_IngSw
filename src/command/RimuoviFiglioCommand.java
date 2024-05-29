package command;

import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import memento.PannelloDisegnoMemento;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RimuoviFiglioCommand implements Command{

    private OrganigrammaElement elementoDaElimin,padre;
    private HashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti=new HashMap<>();

    public RimuoviFiglioCommand(OrganigrammaElement padre, OrganigrammaElement elementoDaElimin) {
        this.padre=padre;
        this.elementoDaElimin=elementoDaElimin;
        LinkedList<OrganigrammaElement> figliOrgano=new LinkedList<>();
        for(OrganigrammaElement figlio: elementoDaElimin.getChild()){
            figliOrgano.add(figlio);//aggiungo unita
            LinkedList<OrganigrammaElement> figliDeiFigli=new LinkedList<>();
            figliDeiFigli.addAll(figlio.getChild());//aggiunto tutte le sottunita di quella unita
            figliPresenti.put(figlio, figliDeiFigli);
            trovaFigliUlteriori(figlio.getChild());
        }
        figliPresenti.put(elementoDaElimin,figliOrgano);
    }

    private void trovaFigliUlteriori(Collection<OrganigrammaElement> figli) {
        for(OrganigrammaElement elem: figli){
            if(!elem.getChild().isEmpty()){
                Collection<OrganigrammaElement> listaFigli=elem.getChild();
                LinkedList<OrganigrammaElement> figliDeiFigli=new LinkedList<>();
                figliDeiFigli.addAll(elem.getChild());
                figliPresenti.put(elem,figliDeiFigli);
                trovaFigliUlteriori(listaFigli);
            }
        }
    }


    @Override
    public boolean doIt() {
        try {
            padre.removeChild(elementoDaElimin);
        } catch (SubjectSenzaListenerInAscoltoException | FiglioNonPresenteInQuestaUnitaException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    @Override
    public boolean undoIt() {
        try{padre.addChild(elementoDaElimin);
            for(Map.Entry entry: figliPresenti.entrySet()){
                OrganigrammaElement padre=(OrganigrammaElement) entry.getKey();
                LinkedList<OrganigrammaElement> figli=(LinkedList<OrganigrammaElement>) entry.getValue();

                    for(OrganigrammaElement elem :figli)
                        padre.addChild(elem);
                } }catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException e) {
                    throw new RuntimeException(e);
                }
        return true;
    }
}
