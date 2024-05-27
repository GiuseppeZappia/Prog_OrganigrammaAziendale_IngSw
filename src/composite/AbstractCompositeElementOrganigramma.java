package composite;

import exceptions.*;
import mediator.ChangeManagerMediator;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import observer.CambiamentoUnitaListener;

import java.util.*;

public abstract class AbstractCompositeElementOrganigramma implements OrganigrammaElement {

    //MESSO PROTETTO PERCHE COSI METTO DALLE CLASSE FIGLIE THIS ANCHE QUA DENTRO E LO POSSO ELIMINARE, TIPO ORGANOGESTIONE
    //NEL COSTRUTTORE SI INSERISCE E SE VOLESSI ELIMINARLO POSSO FARLO, VA BENE?? LO INSERISCO SOLO PER ORGANOGESTIONE
    //VISTO CHE PER GLI ALTRI MI BASTA CHIAMARE RIMUOVI SU PADRE
    protected ArrayList<OrganigrammaElement> elements = new ArrayList<>();
    private ArrayList<Ruolo> ruoliDisponibili = new ArrayList<>();
    private ArrayList<Dipendente> dipendentiUnita = new ArrayList<>();
    private HashMap<Dipendente, Ruolo> personaleUnita = new HashMap<>();
    private ArrayList<CambiamentoUnitaListener> listeners = new ArrayList<>();

    protected abstract ChangeManagerMediator getMediatore(); //factoryMethod????
                                                             //lo lascio???????
                                                             //NON DOVREBBE ESSERE FACTORY

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
        LinkedList<Dipendente> dipendentiToChangeRole = findDipendentiWithRuolo(r);
        this.notifyRuoloChanged(r, dipendentiToChangeRole);
        //cosi l'interfaccia apre una finestra per ogni dipendente interessato, al massimo li passo da qui
        return true;
    }

    //trovo dipendenti con ruolo eliminato per farglielo cambiare
    private LinkedList<Dipendente> findDipendentiWithRuolo(Ruolo r) {
        LinkedList<Dipendente> ret = new LinkedList<>();
        for (Map.Entry<Dipendente, Ruolo> entry : personaleUnita.entrySet()) {
            if (entry.getValue().equals(r)) {
                ret.add(entry.getKey());
            }
        }
        return ret;
    }


    @Override
    public boolean addDipendenti(Dipendente d) throws DipendenteGiaEsistenteException {
        if (dipendentiUnita.contains(d)) {
            throw new DipendenteGiaEsistenteException();
        }
        //CONTROLLO SE RUOLO È VALIDO OPPURE NO VISTO CHE LO FACCIO SCEGLIERE TRA QUELLI PRESENTI??
        personaleUnita.put(d, d.getRuolo(this));//LO INSERISCO NEL PERSONALE
        return dipendentiUnita.add(d);//LO INSERISCO TRA I DIPENDENTI
    }

    @Override
    public boolean removeDipendenti(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if (!dipendentiUnita.contains(d)) {
            throw new DipendenteNonPresenteNellUnitaException();
        }
        removePersonale(d);//ELIMINO DAL PERSONALE IL DIPENDENTE
        d.rimuoviDipendenteDaUnita(this);
        return dipendentiUnita.remove(d);
    }


    //METTO PRIVATO SE USO SOLO IO
    private boolean removePersonale(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if (!personaleUnita.containsKey(d)) {
            throw new DipendenteNonPresenteNellUnitaException();
        }
        //QUA CONTROLLO SU RUOLO LO FACCIO PASSANDO COME PARAMETRO ANCHE RUOLO UTENTE?
        // O COME PRIMA VISTO CHE TANTO LO CHIAMO IO PASSANDO
        //IL GET RUOLO DEL CLIENTE CHE SO ESSERE VALIDO NON NE VALE LA PENA?
        personaleUnita.remove(d);
        return true;
    }


    //NON USO ADDPERSONALE PERCHE VOGLIO CHE QUESTO SIA CHIAMATO SOLO QUANDO SONO SICURO CHE ANCHE NELL'OGGETTO DIPENDENTE IL RUOLO
    //SIA STATO CAMBIATO INOLTRE QUA DEVO CONTROLLARE CHE DIPENDENTE CI SIA GIA TRA IL PERSONALE SOPRA INVECE NO
    @Override
    public boolean changeRuoloToDipendente(Dipendente d,Ruolo nuovo) throws DipendenteNonPresenteNellUnitaException {
        if (!dipendentiUnita.contains(d)) {
            throw new DipendenteNonPresenteNellUnitaException();
        }
        personaleUnita.put(d, nuovo);//COSI LO AGGIORNO COL NUOVO RUOLO E SO CHE QUESTO METODO LO CHIAMO IO
        return true;
    }


    //METODI DI AGGIUNTA LISTENERS E NOTIFY
    public void notifyRuoloChanged(Ruolo r, LinkedList<Dipendente> dipendentiToChangeRole) throws SubjectSenzaListenerInAscoltoException {
        getMediatore().notifyRemovedRuolo(this,r,dipendentiToChangeRole); //MEDIATORE SI OCCUPA DI AVVISARE I LISTENER CHE PER QUESTO SUBJECT UN RUOLO È VARIATO
    }


    public void notifyAddedChild(OrganigrammaElement padre, OrganigrammaElement figlio) throws SubjectSenzaListenerInAscoltoException {
        getMediatore().notifyAddedChild(padre, figlio);//MEDIATORE SI OCCUPA DI AVVISARE I LISTENER DI QUEL SUBJECT CHE FIGLIO È VARIATO
    }

    @Override
    public void addListener(CambiamentoUnitaListener l) {
        getMediatore().registerListenerForSubject(this, l);
    }

    @Override
    public void removeListener(CambiamentoUnitaListener l) {
        getMediatore().unregisterListenerForSubject(this, l);
    }


    public void notifyRemovedChild(OrganigrammaElement figlioEliminato) throws SubjectSenzaListenerInAscoltoException {
        getMediatore().notifyRemovedChild(this, figlioEliminato);
    }

    @Override
    public Collection<String> stampaFigli(){
        ArrayList<String> ret = new ArrayList<>();
        Iterator<OrganigrammaElement> it=elements.iterator();
        while(it.hasNext()){
            OrganigrammaElement o=it.next();
            ret.add(o.getNome());
        }
        return ret;
    }

    @Override
    public Iterator<OrganigrammaElement> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<OrganigrammaElement> {
        Iterator<OrganigrammaElement> it = elements.iterator(); //METTO ITERATOR E NON LIST???
        private OrganigrammaElement last = null;

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public OrganigrammaElement next() {
            last = it.next();
            return last;
        }

        @Override
        public void remove() {
            if (last == null) {
                throw new NoSuchElementException();
            }
            it.remove();
            last = null;
        }


    }
}
