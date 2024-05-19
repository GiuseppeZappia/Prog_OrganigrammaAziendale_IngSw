package Composite;


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
    public boolean addChild(OrganigrammaElement element){
        throw new UnsupportedOperationException("LA SEGUENTE UNITA NON PERMETTE L'AGGIUNTA DI FIGLI");
    }

    @Override
    public boolean removeChild(OrganigrammaElement element) {
        throw new UnsupportedOperationException("LA SEGUENTE UNITA NON PREVEDE LA PRESENZA DI FIGLI");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("LA SEGUENTE UNITA NON PREVEDE LA PRESENZA DI FIGLI");
    }

    @Override
    public Collection<OrganigrammaElement> getChild() {
        throw new UnsupportedOperationException("LA SEGUENTE UNITA NON PREVEDE LA PRESENZA DI FIGLI");
    }

    @Override
    public Collection<String> stampaFigli(){
       // throw new UnsupportedOperationException("LA SEGUENTE UNITA NON PREVEDE LA PRESENZA DI FIGLI");
        System.out.println("LA SEGUENTE UNITA NON PREVEDE LA PRESENZA DI FIGLI");
        return null;//poi togli ste due righe usate per debug
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
