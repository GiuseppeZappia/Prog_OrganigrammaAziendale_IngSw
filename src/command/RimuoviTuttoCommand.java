package command;

import composite.OrganigrammaElement;
import composite.OrganoGestione;
import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import gui.PannelloDisegno;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RimuoviTuttoCommand implements Command {
    private OrganoGestione orgGest;
    private PannelloDisegno pd;
    private HashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti=new HashMap<>();


    public RimuoviTuttoCommand(PannelloDisegno pd, OrganoGestione orgGest) {
        this.pd=pd;
        this.orgGest = orgGest;
        LinkedList<OrganigrammaElement> figliOrgano=new LinkedList<>();
        for(OrganigrammaElement elem: orgGest.getChild()){
            figliOrgano.add(elem);//aggiungo unita
            LinkedList<OrganigrammaElement> figliDeiFigli=new LinkedList<>();
            figliDeiFigli.addAll(elem.getChild());//aggiunto tutte le sottunita di quella unita
            figliPresenti.put(elem, figliDeiFigli);
            trovaFigliUlteriori(elem.getChild());
        }
        figliPresenti.put(orgGest,figliOrgano);
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
            orgGest.rimuoviTutto();
        } catch (FiglioNonPresenteInQuestaUnitaException | SubjectSenzaListenerInAscoltoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean undoIt() {
        pd.aggiungiUnita(orgGest);//disegno organo
        for(Map.Entry entry: figliPresenti.entrySet()){
            OrganigrammaElement padre=(OrganigrammaElement) entry.getKey();
            LinkedList<OrganigrammaElement> figli=(LinkedList<OrganigrammaElement>) entry.getValue();
            try{
                for(OrganigrammaElement elem :figli)
                    padre.addChild(elem);
            } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}