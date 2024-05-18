package Composite;

import Exceptions.*;
import Mediator.ChangeManagerMediator;
import Utilities.Dipendente;
import Utilities.Ruolo;
import Observer.CambiamentoUnitaListener;
import java.util.*;

public abstract class AbstractCompositeElementOrganigramma implements OrganigrammaElement {

    //MESSO PROTETTO PERCHE COSI METTO DALLE CLASSE FIGLIE THIS ANCHE QUA DENTRO E LO POSSO ELIMINARE, TIPO ORGANOGESTIONE
    //NEL COSTRUTTORE SI INSERISCE E SE VOLESSI ELIMINARLO POSSO FARLO, VA BENE?? LO INSERISCO SOLO PER ORGANOGESTIONE
    //VISTO CHE PER GLI ALTRI MI BASTA CHIAMARE RIMUOVI SU PADRE
    protected ArrayList<OrganigrammaElement> elements=new ArrayList<>();
    private ArrayList<Ruolo> ruoliDisponibili=new ArrayList<>();
    private ArrayList<Dipendente> dipendentiUnita =new ArrayList<>();
    private HashMap<Dipendente,Ruolo> personaleUnita=new HashMap<>();
    private ArrayList<CambiamentoUnitaListener> listeners=new ArrayList<>();
    private ChangeManagerMediator mediator=creaMediatore();


    protected abstract ChangeManagerMediator creaMediatore(); //factoryMethod, lo lascio??????? posso passargli parametro, ha senso??

    @Override
    public boolean addChild(OrganigrammaElement element) throws FiglioUnitaNonValidoException {
        return elements.add(element);
    }

    @Override
    public boolean removeChild(OrganigrammaElement element) {
        return elements.remove(element);
    }

    @Override
    public int getChildCount() {
        return elements.size();
    }

    @Override
    public OrganigrammaElement getChild(int index) {
        return elements.get(index);
    }

    @Override
    public Collection<Ruolo> getRuoliDisponibili(){
        return new ArrayList<>(ruoliDisponibili);  //RESTITUSICO UNA COPIA, È NECESSARIO??
    }

    @Override
    public Collection<Dipendente> getDipendenti(){
        return new ArrayList<>(dipendentiUnita); //ANCHE QUI UNA COPIA
    }

    @Override
    public Map<Dipendente,Ruolo> getPersonale(){
        return new HashMap<>(personaleUnita);//ANCHE QUI UNA COPIA
    }

    @Override
    public boolean addRuolo(Ruolo r) throws RuoloGiaPresenteNellUnitaException {
        if(ruoliDisponibili.contains(r)){
            throw new RuoloGiaPresenteNellUnitaException();
        }
        return ruoliDisponibili.add(r);
    }

    @Override
    public boolean removeRuolo(Ruolo r) throws RuoloNonPresenteNellUnitaException {
        if(!ruoliDisponibili.contains(r)){
            throw new RuoloNonPresenteNellUnitaException();
        }
        //CASO IN CUI HO UN SOLO RUOLO E LO STO ELIMINANDO, COME CAMBIANO DIPENDENTI?
        //NE AGGIUNGO UNO IO E ASSEGNO QUELLO? OPPURE CHIEDO DI DEFINIRNE UNO NUOVO?
        //OPPURE NON MI INTERESSA PERCHE IO QUANDO CREO DIPENDENTE DEVO PASSARE AL COSTRUTTORE ANCHE IL RUOLO, E FINCHE NON LO FACCIO NON
        //PUO ESSERE INSERITO, QUINDI SE NON HO RUOLI NON PUO NEMMENO INSERIRE DIPENDENTE, DEVE VENIRE A CREARE RUOLO (QUESTO MAGARI LO POSSO
        //SCRIVERE NELLA SEZIONE DEL TEMPLATE ASSUNZIONI TIPO
        // if(ruoliDisponibili.size()==1){}

        //SE RIMUOVO UN RUOLO TUTTI I DIPENDENTI CHE HANNO QUEL RUOLO DEVONO ESSERE AGGIORNATI
        //MAGARI POSSO INSERIRE UN RUOLO TIPO "DA ASSEGNARE" DI BASE, COSI POI SI PUO FARE LA CAMBIA RUOLO SULL'UTENTE
        //OPPURE POTREI DOVER USARE IL COMMAND PER FARMI CAMBIARE RUOLO TIPO
        ruoliDisponibili.remove(r);
        LinkedList<Dipendente> dipendentiToChangeRole=findDipendentiWithRuolo(r);
        this.notificaCambioRuolo(r,dipendentiToChangeRole);
        //cosi l'interfaccia apre una finestra per ogni dipendente interessato, al massimo li passo da qui
        return true;
    }


    //trovo dipendenti con ruolo eliminato per farglielo cambiare
    private LinkedList<Dipendente> findDipendentiWithRuolo(Ruolo r){
        LinkedList<Dipendente> ret=new LinkedList<>();
        for(Map.Entry<Dipendente,Ruolo> entry: personaleUnita.entrySet()){
            if(entry.getValue().equals(r)){
                ret.add(entry.getKey());
            }
        }
        return ret;
    }


    @Override
    public boolean addDipendenti(Dipendente d) throws DipendenteGiaEsistenteException {
        if(dipendentiUnita.contains(d)){
            throw new DipendenteGiaEsistenteException();
        }
        //CONTROLLO SE RUOLO È VALIDO OPPURE NO VISTO CHE LO FACCIO SCEGLIERE TRA QUELLI PRESENTI??

        personaleUnita.put(d,d.getRuolo());//LO INSERISCO NEL PERSONALE
        return dipendentiUnita.add(d);//LO INSERISCO TRA I DIPENDENTI
    }

    @Override
    public boolean removeDipendenti(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if(!dipendentiUnita.contains(d)){
            throw new DipendenteNonPresenteNellUnitaException();
        }
        removePersonale(d);//ELIMINO DAL PERSONALE IL DIPENDENTE
        return dipendentiUnita.remove(d);
    }

    //METTO PRIVATO SE USO SOLO IO
    @Override
    public void addPersonale(Dipendente d, Ruolo r) throws DipendenteConRuoloGiaAssegnatoInTaleUnitaException {
        if(personaleUnita.containsKey(d)){//IN UNA UNITA ORGANIZZATIVA DIPENDENTE PUO AVERE UN SOLO RUOLO
            throw new DipendenteConRuoloGiaAssegnatoInTaleUnitaException();
        }
        personaleUnita.put(d,r);
    }

    //NON USO ADDPERSONALE PERCHE VOGLIO CHE QUESTO SIA CHIAMATO SOLO QUANDO SONO SICURO CHE ANCHE NELL'OGGETTO DIPENDENTE IL RUOLO
    //SIA STATO CAMBIATO INOLTRE QUA DEVO CONTROLLARE CHE DIPENDENTE CI SIA GIA TRA IL PERSONALE SOPRA INVECE NO
    @Override
    public boolean changeRuoloToDipendente(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if(!dipendentiUnita.contains(d)){
            throw new DipendenteNonPresenteNellUnitaException();
        }
        personaleUnita.put(d,d.getRuolo());//COSI LO AGGIORNO COL NUOVO RUOLO E SO CHE QUESTO METODO LO CHIAMO IO
        return true;
    }


    //METTO PRIVATO SE USO SOLO IO
    @Override
    public boolean removePersonale(Dipendente d) throws DipendenteNonPresenteNellUnitaException {
        if(!personaleUnita.containsKey(d)){
            throw new DipendenteNonPresenteNellUnitaException();
        }
        //QUA CONTROLLO SU RUOLO LO FACCIO PASSANDO COME PARAMETRO ANCHE RUOLO UTENTE?
        // O COME PRIMA VISTO CHE TANTO LO CHIAMO IO PASSANDO
        //IL GET RUOLO DEL CLIENTE CHE SO ESSERE VALIDO NON NE VALE LA PENA?
        personaleUnita.remove(d);
        return true;
    }


    //METODI DI AGGIUNTA LISTENERS E NOTIFY
    public void notificaCambioRuolo(Ruolo r,LinkedList<Dipendente> dipendentiToChangeRole){
        mediator.notifyRemovedRuolo(this,r,dipendentiToChangeRole); //MEDIATORE SI OCCUPA DI AVVISARE I LISTENER CHE PER QUESTO SUBJECT UN RUOLO È VARIATO
    }


    public void notifyAddedChild(OrganigrammaElement padre, OrganigrammaElement figlio){
        mediator.notifyAddedChild(padre,figlio);//MEDIATORE SI OCCUPA DI AVVISARE I LISTENER DI QUEL SUBJECT CHE FIGLIO È VARIATO
    }

    public void addListener(CambiamentoUnitaListener l){
        mediator.registerListenerForSubject(this,l);
    }

    public void removeListener(CambiamentoUnitaListener l){
       mediator.unregisterListenerForSubject(this,l);
    }

    public void notifyRemovedChild(OrganigrammaElement padre, OrganigrammaElement figlioEliminato){
        mediator.notifyRemovedChild(this,figlioEliminato);
    }

    @Override
    public Iterator<OrganigrammaElement> iterator(){
        return new MyIterator();
    }

    private class MyIterator implements Iterator<OrganigrammaElement>{
        Iterator<OrganigrammaElement> it=elements.iterator(); //METTO ITERATOR E NON LIST???
        private OrganigrammaElement last=null;

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public OrganigrammaElement next() {
            last=it.next();
            return last;
        }

        @Override
        public void remove() {
            if(last==null){
                throw new NoSuchElementException();
            }
            it.remove();
            last=null;
        }




    }
}
