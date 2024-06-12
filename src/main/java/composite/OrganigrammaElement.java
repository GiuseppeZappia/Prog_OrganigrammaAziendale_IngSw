package composite;

import exceptions.*;
import observer.CambiamentoUnitaListener;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;


import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface OrganigrammaElement extends Serializable {


    boolean addRuolo(Ruolo r) throws RuoloGiaPresenteNellUnitaException;
    boolean removeRuolo(Ruolo r) throws RuoloNonPresenteNellUnitaException, SubjectSenzaListenerInAscoltoException;
    boolean addDipendente(Dipendente d) throws DipendenteGiaEsistenteException;
    boolean removeDipendente(Dipendente d) throws DipendenteNonPresenteNellUnitaException;
    Collection<Ruolo> getRuoliDisponibili();
    Collection<Dipendente> getDipendenti();
    Map<Dipendente,Ruolo> getPersonale();
    boolean changeRuoloToDipendente(Dipendente d,Ruolo nuovo) throws DipendenteNonPresenteNellUnitaException;
    boolean addChild(OrganigrammaElement toAdd) throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException;
    boolean removeChild(OrganigrammaElement toRemove) throws SubjectSenzaListenerInAscoltoException, FiglioNonPresenteInQuestaUnitaException;
    int getChildCount();
    Collection<OrganigrammaElement> getChild();
    String getNome();
    void rimuoviFigliTutti();
    void removeAllListeners() throws SubjectSenzaListenerInAscoltoException;
    void addListener(CambiamentoUnitaListener c);
    void removeListener(CambiamentoUnitaListener c) throws SubjectSenzaListenerInAscoltoException;
}