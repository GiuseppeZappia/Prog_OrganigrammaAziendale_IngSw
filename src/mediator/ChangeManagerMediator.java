package mediator;


import composite.OrganigrammaElement;
import exceptions.SubjectSenzaListenerInAscoltoException;
import observer.CambiamentoUnitaListener;

import java.io.Serializable;

public interface ChangeManagerMediator extends Serializable {
    void registerListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void unregisterListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void unregisterAll(OrganigrammaElement subject) throws SubjectSenzaListenerInAscoltoException;
    void notifyAddedChild(OrganigrammaElement subject,OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException;
    void notifyRemovedChild(OrganigrammaElement subject,OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException;


    //da eliminare
    void stampa(OrganigrammaElement ele);

}
