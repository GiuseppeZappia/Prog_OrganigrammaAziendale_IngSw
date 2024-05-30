package composite;

import exceptions.FiglioNonPresenteInQuestaUnitaException;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import mediator.ChangeManagerMediator;
import observer.CambiamentoUnitaListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class UnitaOrganizzativa extends AbstractCompositeElementOrganigramma{
    private String nome;
    private ChangeManagerMediator mediatore;

    public UnitaOrganizzativa(String nome,ChangeManagerMediator mediatore) {
        this.nome = nome;
        this.mediatore = mediatore;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override//protected perche è factory???
    protected ChangeManagerMediator getMediatore() {
        return this.mediatore;
    }

    @Override
    public boolean addChild(OrganigrammaElement element) throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException {
        if(!(element instanceof UnitaOrganizzativa)){
            throw new FiglioUnitaNonValidoException();
        }
        //QUESTO IF AGGIUNTO EVENTUALMENTE PER MEMENTO
        if(elements.contains(element)){
            return false;
        }
        boolean inserimento=super.addChild(element);
        return inserimento;
    }

    @Override
    public void addListener(CambiamentoUnitaListener l) {
        super.addListener(l);
    }

    @Override
    public void removeListener(CambiamentoUnitaListener l) {
        super.removeListener(l);
    }

    @Override
    public boolean removeChild(OrganigrammaElement daEliminare) throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        if(! this.getElements().contains(daEliminare)){
            throw new FiglioNonPresenteInQuestaUnitaException();
        }

        return super.removeChild(daEliminare);//cosi se arrivo qui quello è un mio figlio e lo elimino nella abstract
    }

    protected void rimuoviFigli() throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        if(this.getChild().isEmpty()){
            return;
        }
        Collection<OrganigrammaElement> figli=this.getChild();//NON USO THIS.ELEMENTS OPPURE POTREI AVERE LA CONCURRENT MODIFICATION EXCEPTION VISTO CHE PERCORRO LISTA MENTRE CI ELIMINO SU
        for(OrganigrammaElement s:figli){
            UnitaOrganizzativa figlio=(UnitaOrganizzativa)s;
            figlio.rimuoviFigli();
            removeChild(figlio);
        }
    }

    public void rimuoviTutti() throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        this.rimuoviFigli();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitaOrganizzativa that = (UnitaOrganizzativa) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(mediatore, that.mediatore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, mediatore);
    }

    @Override
    public String toString() {
        return "UnitaOrganizzativa{" +
                "nome='" + nome + '\'' +
                '}';
    }


}