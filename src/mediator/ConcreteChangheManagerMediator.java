package mediator;

import composite.OrganigrammaElement;
import exceptions.SubjectSenzaListenerInAscoltoException;
import observer.CambiamentoUnitaListener;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;

import java.util.HashMap;
import java.util.LinkedList;

public class ConcreteChangheManagerMediator implements ChangeManagerMediator {
    //PER OGNI UNITA HO LA LISTA DEI SUOI LISTENER
    private HashMap<OrganigrammaElement, LinkedList<CambiamentoUnitaListener>> listenersForSubject = new HashMap<>();

    public ConcreteChangheManagerMediator() {}

    //FAI CONTROLLO SE ESISTE SUBJECT SU TUTTI


    @Override
    public void registerListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer) {
        if (!listenersForSubject.containsKey(subject)){//SE NON C'È PROPRIO QUEL SUBJECT LO AGGIUNGO
            listenersForSubject.put(subject,new LinkedList<>());
            listenersForSubject.get(subject).add(observer);
        }
        if(!listenersForSubject.get(subject).contains(observer)) {
            //SE C'È IL SUBJECT E NON HA QUEL LISTENER LO AGGIUNGO
            listenersForSubject.get(subject).add(observer);
        }
    }

    @Override
    public void unregisterListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer) {
        listenersForSubject.get(subject).remove(observer);
    }

    @Override
    public void notifyAddedChild(OrganigrammaElement subject, OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException {
        if (!listenersForSubject.containsKey(subject)) {
            throw new SubjectSenzaListenerInAscoltoException();
        }
        for (CambiamentoUnitaListener listener : listenersForSubject.get(subject)) {
            listener.inseritoFiglio(subject, child);
        }
    }


    @Override
    public void notifyRemovedRuolo(OrganigrammaElement subject, Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole) throws SubjectSenzaListenerInAscoltoException {
        if (!listenersForSubject.containsKey(subject)) {
            throw new SubjectSenzaListenerInAscoltoException();
        }
        for (CambiamentoUnitaListener listener : listenersForSubject.get(subject)) {
            //listener.ruoloCambiato(r, dipendentiToChangeRole);//COSI ELEMENTO DELLA GUI RICEVE CAMBIAMENTO E PROCEDE CON LE SUE OPERAZIONI
        }
    }

    @Override
    public void notifyRemovedChild(OrganigrammaElement subject, OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException {
        if (!listenersForSubject.containsKey(subject)) {
            throw new SubjectSenzaListenerInAscoltoException();
        }
        for (CambiamentoUnitaListener listener : listenersForSubject.get(subject)) {
            listener.rimossoFiglio(child);
        }
    }


}
