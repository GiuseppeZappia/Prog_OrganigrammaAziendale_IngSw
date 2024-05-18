package Composite;

import Exceptions.*;
import Utilities.Dipendente;
import Utilities.Ruolo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public interface OrganigrammaElement extends Iterable<OrganigrammaElement> {


    //ADDPERSONALE FUNGE COME AGGIUNGIRUOLO A DIPENDENTE, DEVO VEDERE DI NON LASCIARE DIPENDENTI SENZA RUOLI??
    //MAGARI POTREI FARE SPECIFICARE UNITA E RUOLO QUANDO CREO DIPENDENTE E POI SE GLI VUOLE TOGLIERE RUOLO POTREI
    //FAR COMPARIRE FINESTRA PER SCELTA NUOVO RUOLO E CHIAMARE SETRUOLO IMPLEMENTATO COME SETTA+CHIAMAADDPERSONALE
    boolean addRuolo(Ruolo r) throws RuoloGiaPresenteNellUnitaException;
    boolean removeRuolo(Ruolo r) throws RuoloNonPresenteNellUnitaException;
    boolean addDipendenti(Dipendente d) throws DipendenteGiaEsistenteException;
    boolean removeDipendenti(Dipendente d) throws DipendenteNonPresenteNellUnitaException;
    void addPersonale(Dipendente d, Ruolo r) throws DipendenteConRuoloGiaAssegnatoInTaleUnitaException;
    Collection<Ruolo> getRuoliDisponibili();
    Collection<Dipendente> getDipendenti();
    Map<Dipendente,Ruolo> getPersonale();
    void notificaCambioRuolo(Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole);
    boolean removePersonale(Dipendente d) throws DipendenteNonPresenteNellUnitaException;
    boolean changeRuoloToDipendente(Dipendente d) throws DipendenteNonPresenteNellUnitaException;
    boolean addChild(OrganigrammaElement toAdd) throws FiglioUnitaNonValidoException;
    boolean removeChild(OrganigrammaElement toRemove);
    int getChildCount();
    OrganigrammaElement getChild(int index);



}