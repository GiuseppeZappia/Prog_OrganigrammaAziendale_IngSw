package Command;

import Composite.OrganigrammaElement;
import Composite.OrganoGestione;
import GUI.PannelloDisegno;
import Mediator.ChangeManagerMediator;

import javax.swing.*;

public class DisegnaElementCommand implements Command {
    private final PannelloDisegno pd;
    private final ChangeManagerMediator mediator;
    private OrganigrammaElement element;

    public DisegnaElementCommand(PannelloDisegno p, ChangeManagerMediator mediator) {
        this.pd = p;
        this.mediator=mediator;
    }

    @Override
    public boolean disegna(){
        String nomeOrganoGestione = JOptionPane.showInputDialog(pd, "Nome Organo Gestione:", "Creazione Organo Gestione", JOptionPane.QUESTION_MESSAGE);
        if (nomeOrganoGestione == null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
            return false;
        }
        if (nomeOrganoGestione.trim().isEmpty()) {//SE PROVA AD INVIARE SENZA SCRIVERE NULLA DO ERRORE
            JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        OrganigrammaElement elem = new OrganoGestione(nomeOrganoGestione, mediator);
        this.element=elem;
        pd.aggiungiUnita(elem);
        return true;
    }

    @Override
    public boolean rimuovi(){
        pd.rimuoviUnita(element);
        return true;
    }


}
