package composite;

import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import observer.CambiamentoUnitaListener;

import java.util.*;


public class OrganoGestione extends AbstractCompositeElementOrganigramma {
    private String nome;

    public OrganoGestione(String nome) {
        this.nome = nome;
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
        //QUESTO IF AGGIUNTO PER MEMENTO, IN MODO CHE SE UN FIGLIO C'è GIA NON LO AGGIUNGA DI NUOVO
        if(elements.contains(element)){
            return false;
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
                // SONO SICURO PER COSTRUZIONE DI AVERE SOLO UNITAORGANIZZATIVA IN QUESTA LISTA,
                // GRAZIE AL PATTERN COMPOSITE CHE AVREBBE LANCIATO ECCEZIONE AL MOMEMNTO AGGIUNTA ALTRIMENTI
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
    public void removeListener(CambiamentoUnitaListener l) throws SubjectSenzaListenerInAscoltoException {
        super.removeListener(l);
    }

    //METODO CHE CANCELLA TUTTO ORGANIRGAMMA
    //PER OGNI FIGLIO CHIAMO FUNZIONE CHE RIMUOVE I PROPRI FIGLI
    public void rimuoviTutto() throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        Collection<OrganigrammaElement> listaFigli=this.getChild();
        //NON USO THIS.ELEMENTS OPPURE POTREI AVERE LA CONCURRENT MODIFICATION EXCEPTION
        // VISTO CHE PERCORRO LISTA MENTRE CI ELIMINO SU
        for(OrganigrammaElement elem : listaFigli){
            UnitaOrganizzativa unita=(UnitaOrganizzativa) elem;
            unita.rimuoviFigli();
            this.removeChild(elem);
        }
        super.removeChild(this);
        //cosi rimuovo anche organogestione, lo posso chiamare nonostante non sia presente nella lista
        //element perche tanto la funzione della super classe avverte col mediatore i miei listener che
        // sono stato eliminato
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganoGestione that = (OrganoGestione) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "OrganoGestione{" +
                "nome='" + nome + '\'' +
                '}';
    }
}