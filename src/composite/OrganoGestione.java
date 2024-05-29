package composite;

import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import mediator.ChangeManagerMediator;
import memento.PannelloDisegnoMemento;
import observer.CambiamentoUnitaListener;

import java.util.*;


public class OrganoGestione extends AbstractCompositeElementOrganigramma {
    private String nome;
    //EVENTUALMENTE TRANSIENT
    private ChangeManagerMediator mediatore;

    public OrganoGestione(String nome,ChangeManagerMediator mediatore) {
        this.nome = nome;
        this.mediatore = mediatore;
    }

    //AGGIUNTO PER MEMENTO PROVE UNDO
    public OrganoGestione(OrganoGestione o) {
        this.nome = o.getNome();
        this.mediatore= o.getMediatore();
        this.elements=new ArrayList<>(o.getElements());
    }

    public void stampaMiei(){
        mediatore.stampa(this);
    }

    @Override//protected perche è factory???
    protected ChangeManagerMediator getMediatore() {
        return this.mediatore;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public boolean addChild(OrganigrammaElement element) throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException {
        if(!(element instanceof UnitaOrganizzativa)){
            throw new FiglioUnitaNonValidoException();
        }
        return super.addChild(element);
    }

    @Override
    public boolean removeChild(OrganigrammaElement daEliminare) throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        if(!this.getElements().contains(daEliminare)){
            throw new FiglioNonPresenteInQuestaUnitaException();
        }//SONO SICURO SIA UN FIGLIO

        //DEVO ADESSO ELIMINARE UNO ALLA VOLTA TUTTI I FIGLI DEL FIGLIO FINO ALLE FOGLIE DELL'ORGANIGRAMMA
        for (OrganigrammaElement elem : getElements()) {
            if (elem.equals(daEliminare)) {//UNA VOLTA TROVATO QUELLO DA ELIIMINARE...
//
//                //SONO SICURO PER COSTRUZIONE DI AVERE SOLO UNITAORGANIZZATIVA IN QUESTA LISTA, GRAZIE AL PATTERN COMPOSITE
//                //CHE AVREBBE LANCIATO ECCEZIONE AL MOMEMNTO AGGIUNTA ALTRIMENTI
                ((UnitaOrganizzativa) elem).rimuoviFigli(); //QUESTO METODO NELLA CLASSE UNITAORGANIZZATIVA FA RIMOZIONE DEI SUOI FIGLI
            }
        }
        super.removeChild(daEliminare); //ELIMINO POI DA ME L'OGGETTO FIGLIO INIZIALE
        return false;
    }


    @Override
    public void addListener(CambiamentoUnitaListener l) {
        super.addListener(l);
    }

    @Override
    public void removeListener(CambiamentoUnitaListener l) {
        super.removeListener(l);
    }


    //METODO CHE CANCELLA TUTTO ORGANIRGAMMA, AVEVO INSERITO ORGANOGESTIONE IN TESTA AD ELEMENTS,MI BASTA PRIMA CHIAMARE LA REMOVE
    // PER TUTTI I FIGLI E POI SVUOTARE LA LISTA ELEMENTS
    //PERCHE TANTO NELLA GUI IMPLEMENTO DISEGNO SCORRENDO ELEMENTI PRESENTI IN QUELLA E FACENDOLI DISEGNARE??
    public void rimuoviTutto() throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        Collection<OrganigrammaElement> listaFigli=this.getChild();//NON USO THIS.ELEMENTS OPPURE POTREI AVERE LA CONCURRENT MODIFICATION EXCEPTION VISTO CHE PERCORRO LISTA MENTRE CI ELIMINO SU
        for(OrganigrammaElement elem : listaFigli){
            UnitaOrganizzativa unita=(UnitaOrganizzativa) elem;
            unita.rimuoviFigli();
            this.removeChild(elem);
        }
        super.removeChild(this);//cosi rimuovo anche organogestione, lo posso chiamare nonostante non sia presente nella lista
                                        //element perche tanto la funzione della super classe avverte col mediatore i miei listener che sono staot eliminato
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganoGestione that = (OrganoGestione) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(mediatore, that.mediatore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, mediatore);
    }

    @Override
    public String toString() {
        return "OrganoGestione{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
