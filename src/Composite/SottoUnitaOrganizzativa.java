package Composite;


import Exceptions.FiglioNonPresenteInQuestaUnitaException;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import Mediator.ChangeManagerMediator;
import Observer.CambiamentoUnitaListener;

import java.util.Collection;
import java.util.Objects;

public class SottoUnitaOrganizzativa extends AbstractCompositeElementOrganigramma {
    private String nome;
    private ChangeManagerMediator mediator;

    public SottoUnitaOrganizzativa(String nome, ChangeManagerMediator mediator) {
        this.nome = nome;
        this.mediator = mediator;
    }


    @Override
    protected ChangeManagerMediator getMediatore() {
        return this.mediator;
    }

    @Override
    public boolean addChild(OrganigrammaElement element) throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException {
        if(!(element instanceof SottoUnitaOrganizzativa)){
            throw new FiglioUnitaNonValidoException();
        }
        boolean inserimento=super.addChild(element);
        return inserimento;
    }

    @Override
    public boolean removeChild(OrganigrammaElement daEliminare) throws FiglioNonPresenteInQuestaUnitaException, SubjectSenzaListenerInAscoltoException {
        if(! this.elements.contains(daEliminare)){
            throw new FiglioNonPresenteInQuestaUnitaException();
        }
        return super.removeChild(daEliminare);//cosi se arrivo qui quello è un mio figlio e lo elimino nella abstract
    }

    @Override
    public String getNome() {
        return nome;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SottoUnitaOrganizzativa that = (SottoUnitaOrganizzativa) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(mediator, that.mediator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, mediator);
    }

    @Override
    public String toString() {
        return "SottoUnitaOrganizzativa{" +
                "nome='" + nome + '\'' +
                ", mediator=" + mediator +
                '}';
    }

}
