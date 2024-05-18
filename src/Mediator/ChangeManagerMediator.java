package Mediator;


import Composite.AbstractCompositeElementOrganigramma;
import Composite.OrganigrammaElement;
import Composite.SottoUnitaOrganizzativa;
import Observer.CambiamentoUnitaListener;
import Utilities.Dipendente;
import Utilities.Ruolo;

import javax.security.auth.Subject;
import java.util.LinkedList;

public interface ChangeManagerMediator {
    void registerListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void unregisterListenerForSubject(OrganigrammaElement subject, CambiamentoUnitaListener observer);
    void notifyAddedChild(OrganigrammaElement subject,OrganigrammaElement child);
    void notifyRemovedRuolo(OrganigrammaElement subject, Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole);
    void notifyRemovedChild(OrganigrammaElement subject,OrganigrammaElement child);

}