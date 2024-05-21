package Command;

import Composite.OrganigrammaElement;
import Composite.OrganoGestione;
import Composite.UnitaOrganizzativa;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import GUI.PannelloDisegno;
import Mediator.ChangeManagerMediator;

import javax.swing.*;
import java.util.LinkedList;

public class AggiungiFiglioOrganoCommand implements Command {

    private PannelloDisegno pd;
    private ChangeManagerMediator mediator;

    public AggiungiFiglioOrganoCommand(PannelloDisegno p, ChangeManagerMediator m) {
        this.pd=p;
        this.mediator=m;
    }


//controlla bene return per undo e redo
    @Override
    public boolean disegna() {
        if (pd.getUnitaDisegnate().isEmpty()) {
            JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza aver inserito un organo gestione", "Operazione non valida", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String nomeUnitaOrganizzativa = JOptionPane.showInputDialog(pd, "Nome Unità Organizzativa:", "Creazione Unità Organizzativa", JOptionPane.QUESTION_MESSAGE);
        if (nomeUnitaOrganizzativa == null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
            return false;
        }
        if (nomeUnitaOrganizzativa.trim().isEmpty()) {//SE PROVA AD INVIARE SENZA SCRIVERE NULLA DO ERRORE
            JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
        }
        if(nomeGiaPresente(nomeUnitaOrganizzativa,pd.getUnitaDisegnate())){
            JOptionPane.showMessageDialog(pd, "È già presente un'unità con questo nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        OrganigrammaElement unita=new UnitaOrganizzativa(nomeUnitaOrganizzativa,mediator);
        OrganoGestione padre= (OrganoGestione) pd.getUnitaDisegnate().getFirst();//SONO SICURO CHE IL PRIMO ELEMENTO SIA ORGANO GESTIONE PERCHE NON CONSENTO ALTRO INSERIMENTO
        try {
            unita.addListener(pd);//AGGIUNGO IL PANNELLO COME LISTENER COSI ASCOLTA QUANDO CI SONO MODIFICHE SU QUESTO OGGETTO COME RIMOZIONE FIGLI E OPERA DI CONSEGU
            padre.addChild(unita);//observer dentro sto metodo
        } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    @Override
    public boolean rimuovi() {
        return false;
    }

    private boolean nomeGiaPresente(String nome, LinkedList<OrganigrammaElement> presenti){
        for(OrganigrammaElement unita:presenti){
            if(unita.getNome().equals(nome)){
                return true;
            }
        }
        return false;
    }

}
