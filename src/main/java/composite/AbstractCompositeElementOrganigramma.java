package composite;

import exceptions.*;
import observer.mediator.ChangeManagerMediator;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import observer.mediator.ConcreteChangheManagerMediator;
import observer.CambiamentoUnitaListener;

import java.util.*;

public abstract class AbstractCompositeElementOrganigramma implements OrganigrammaElement {

    protected ArrayList<OrganigrammaElement> elements = new ArrayList<>();
    private ArrayList<Ruolo> ruoliDisponibili = new ArrayList<>();
    private ArrayList<Dipendente> dipendentiUnita = new ArrayList<>();
    private HashMap<Dipendente, Ruolo> personaleUnita = new HashMap<>();
    private ChangeManagerMediator mediator= ConcreteChangheManagerMediator.MEDIATOR;

    private static final long serialVersionUID = 1L;

    protected ArrayList<OrganigrammaElement> getElements() {
        return elements;
    }

    @Override
    public abstract String getNome();

    @Override
    public boolean addChild(OrganigrammaElement element) throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException {
        boolean ret=elements.add(element);
        this.notifyAddedChild(this,element);
        return ret;
    }

    @Override
    public boolean removeChild(OrganigrammaElement element) throws SubjectSenzaListenerInAscoltoException, FiglioNonPresenteInQuestaUnitaException {
        boolean ret=elements.remove(element);
        this.notifyRemovedChild(element);
        return ret;
    }

    @Override
    public int getChildCount() {
        return elements.size();
    }

    @Override
    public Collection<OrganigrammaElement> getChild() {
        return new ArrayList<>(elements);
    }

    @Override
    public Collection<Ruolo> getRuoliDisponibili() {
        return new ArrayList<>(ruoliDisponibili);  //RESTITUSICO UNA COPIA, È NECESSARIO??
    }

    @Override
    public Collection<Dipendente> getDipendenti() {
        return new ArrayList<>(dipendentiUnita); //ANCHE QUI UNA COPIA
    }

    @Override
    public Map<Dipendente, Ruolo> getPersonale() {
        return new HashMap<>(personaleUnita);//ANCHE QUI UNA COPIA
    }

    @Override
    public boolean addRuolo(Ruolo r) throws RuoloGiaPresenteNellUnitaException {
        if (ruoliDisponibili.contains(r)) {
            throw new RuoloGiaPresenteNellUnitaException();
        }
        return ruoliDisponibili.add(r);
    }

    @Override
    public boolean removeRuolo(Ruolo r) throws RuoloNonPresenteNellUnitaException, SubjectSenzaListenerInAscoltoException {
        if (!ruoliDisponibili.contains(r)) {
            throw new RuoloNonPresenteNellUnitaException();
        }
        ruoliDisponibili.remove(r);
        return true;
    }

    @Override
    public boolean addDipendente(Dipendente d) throws DipendenteGiaEsistenteException {
        if (dipendentiUnita.contains(d)) {
            throw new DipendenteGiaEsistenteException();
        }
        personaleUnita.put(d, d.getRuolo(this));//LO INSERISCO NEL PERSONALE
        return dipendentiUnita.add(d);//LO INSERISCO TRA I DIPENDENTI
    }

    @Override
    public boolean removeDipendente(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if (!dipendentiUnita.contains(d)) {
            throw new DipendenteNonPresenteNellUnitaException();
        }
        removePersonale(d);//ELIMINO DAL PERSONALE IL DIPENDENTE
        d.rimuoviDipendenteDaUnita(this);
        return dipendentiUnita.remove(d);
    }


    private boolean removePersonale(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if (!personaleUnita.containsKey(d)) {
            throw new DipendenteNonPresenteNellUnitaException();
        }
        personaleUnita.remove(d);
        return true;
    }

    @Override
    public void rimuoviFigliTutti(){
        elements.clear();
    }

   @Override
    public boolean changeRuoloToDipendente(Dipendente d,Ruolo nuovo) throws DipendenteNonPresenteNellUnitaException {
        if (!dipendentiUnita.contains(d)) {
            throw new DipendenteNonPresenteNellUnitaException();
        }
        personaleUnita.put(d, nuovo);//COSI LO AGGIORNO COL NUOVO RUOLO E SO CHE QUESTO METODO LO CHIAMO IO
        return true;
    }

    @Override
    public void removeAllListeners() throws SubjectSenzaListenerInAscoltoException {
        mediator.unregisterAll(this);
    }

    protected void notifyAddedChild(OrganigrammaElement padre, OrganigrammaElement figlio) throws SubjectSenzaListenerInAscoltoException {
        mediator.notifyAddedChild(padre, figlio);//MEDIATORE SI OCCUPA DI AVVISARE I LISTENER DI QUEL SUBJECT CHE FIGLIO È VARIATO
    }

    @Override
    public void addListener(CambiamentoUnitaListener l) {
        mediator.registerListenerForSubject(this, l);
    }

    @Override
    public void removeListener(CambiamentoUnitaListener l) throws SubjectSenzaListenerInAscoltoException {
        mediator.unregisterListenerForSubject(this, l);
    }


    protected void notifyRemovedChild(OrganigrammaElement figlioEliminato) throws SubjectSenzaListenerInAscoltoException {
        mediator.notifyRemovedChild(this, figlioEliminato);
    }
}