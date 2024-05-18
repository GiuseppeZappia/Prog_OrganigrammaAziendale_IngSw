package Mediator;

import Composite.OrganigrammaElement;
import Observer.CambiamentoUnitaListener;
import Utilities.Dipendente;
import Utilities.Ruolo;

import java.util.HashMap;
import java.util.LinkedList;

public class ConcreteChangheManagerMediator implements ChangeManagerMediator{
    private HashMap<OrganigrammaElement, LinkedList<CambiamentoUnitaListener>> listenersForSubject=new HashMap<>();

    @Override
    public void registerListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer) {
        if(! listenersForSubject.get(subject).contains(observer)){
            listenersForSubject.get(subject).add(observer);
        }
    }

    @Override
    public void unregisterListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer) {
        listenersForSubject.get(subject).remove(observer);
    }

    @Override
    public void notifyAddedChild(OrganigrammaElement subject,OrganigrammaElement child) {
        for(CambiamentoUnitaListener listener : listenersForSubject.get(subject)) {
            listener.inseritoFiglio(subject,child);
        }
    }

    @Override
    public void notifyRemovedRuolo(OrganigrammaElement subject, Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole){
        for(CambiamentoUnitaListener listener : listenersForSubject.get(subject)) {
            listener.ruoloCambiato(r, dipendentiToChangeRole);//COSI ELEMENTO DELLA GUI RICEVE CAMBIAMENTO E PROCEDE CON LE SUE OPERAZIONI, AL MASSIMO POI QUA VEDO
            //SE PASSARE PARAMETRI O MENO
        }
    }

    @Override
    public void notifyRemovedChild(OrganigrammaElement subject,OrganigrammaElement child) {
        for(CambiamentoUnitaListener listener : listenersForSubject.get(subject)) {
            listener.rimossoFiglio(subject,child);
        }
    }





}
