package composite;

import exceptions.*;
import observer.CambiamentoUnitaListener;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;

import java.util.Collection;
import java.util.Map;

public interface OrganigrammaElement extends Iterable<OrganigrammaElement> {


    //ADDPERSONALE FUNGE COME AGGIUNGIRUOLO A DIPENDENTE, DEVO VEDERE DI NON LASCIARE DIPENDENTI SENZA RUOLI??
    //MAGARI POTREI FARE SPECIFICARE UNITA E RUOLO QUANDO CREO DIPENDENTE E POI SE GLI VUOLE TOGLIERE RUOLO POTREI
    //FAR COMPARIRE FINESTRA PER SCELTA NUOVO RUOLO E CHIAMARE SETRUOLO IMPLEMENTATO COME SETTA+CHIAMAADDPERSONALE
    boolean addRuolo(Ruolo r) throws RuoloGiaPresenteNellUnitaException;
    boolean removeRuolo(Ruolo r) throws RuoloNonPresenteNellUnitaException, SubjectSenzaListenerInAscoltoException;
    boolean addDipendenti(Dipendente d) throws DipendenteGiaEsistenteException;
    boolean removeDipendenti(Dipendente d) throws DipendenteNonPresenteNellUnitaException;
    Collection<Ruolo> getRuoliDisponibili();
    Collection<Dipendente> getDipendenti();
    Map<Dipendente,Ruolo> getPersonale();
    //void notificaCambioRuolo(Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole);
    boolean changeRuoloToDipendente(Dipendente d,Ruolo nuovo) throws DipendenteNonPresenteNellUnitaException;
    boolean addChild(OrganigrammaElement toAdd) throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException;
    boolean removeChild(OrganigrammaElement toRemove) throws SubjectSenzaListenerInAscoltoException, FiglioNonPresenteInQuestaUnitaException;
    int getChildCount();
    Collection<OrganigrammaElement> getChild();
    String getNome();
    Collection<String> stampaFigli();
    void addListener(CambiamentoUnitaListener c);
    void removeListener(CambiamentoUnitaListener c);

}