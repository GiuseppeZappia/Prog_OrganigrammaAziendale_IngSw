package mediator;


import composite.OrganigrammaElement;
import exceptions.SubjectSenzaListenerInAscoltoException;
import observer.CambiamentoUnitaListener;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;

import java.util.LinkedList;

public interface ChangeManagerMediator {
    void registerListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void unregisterListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void notifyAddedChild(OrganigrammaElement subject,OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException;
    void notifyRemovedRuolo(OrganigrammaElement subject, Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole) throws SubjectSenzaListenerInAscoltoException;
    void notifyRemovedChild(OrganigrammaElement subject,OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException;

}
