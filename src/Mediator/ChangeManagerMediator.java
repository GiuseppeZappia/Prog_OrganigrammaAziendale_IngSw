package Mediator;


import Composite.AbstractCompositeElementOrganigramma;
import Composite.OrganigrammaElement;
import Composite.SottoUnitaOrganizzativa;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import Observer.CambiamentoUnitaListener;
import Utilities.Dipendente;
import Utilities.Ruolo;

import javax.security.auth.Subject;
import java.util.LinkedList;

public interface ChangeManagerMediator {
    void registerListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void unregisterListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void notifyAddedChild(OrganigrammaElement subject,OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException;
    void notifyRemovedRuolo(OrganigrammaElement subject, Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole) throws SubjectSenzaListenerInAscoltoException;
    void notifyRemovedChild(OrganigrammaElement subject,OrganigrammaElement child) throws SubjectSenzaListenerInAscoltoException;

}
