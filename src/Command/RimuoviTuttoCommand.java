package Command;

import Composite.OrganigrammaElement;
import Composite.OrganoGestione;
import Exceptions.FiglioNonPresenteInQuestaUnitaException;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import GUI.PannelloDisegno;

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
        }
        figliPresenti.put(orgGest,figliOrgano);
    }

    @Override
    public boolean disegna() {
        try {
            orgGest.rimuoviTutto();
        } catch (FiglioNonPresenteInQuestaUnitaException | SubjectSenzaListenerInAscoltoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean rimuovi() {
        pd.aggiungiUnita(orgGest);//disegno organo
        for(Map.Entry entry: figliPresenti.entrySet()){
            OrganigrammaElement padre=(OrganigrammaElement) entry.getKey();
            LinkedList<OrganigrammaElement> figli=(LinkedList<OrganigrammaElement>) entry.getValue();
            try {
                for(OrganigrammaElement elem :figli)
                padre.addChild(elem);
            } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }
}
