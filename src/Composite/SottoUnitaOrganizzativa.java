package Composite;


import Mediator.ChangeManagerMediator;
import Observer.CambiamentoUnitaListener;

public class SottoUnitaOrganizzativa extends AbstractCompositeElementOrganigramma {
    private String nome;
    private ChangeManagerMediator mediator;

    public SottoUnitaOrganizzativa(String nome, ChangeManagerMediator mediator) {
        this.nome = nome;
        this.mediator = mediator;
    }

    @Override
    protected ChangeManagerMediator creaMediatore() {
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
    public OrganigrammaElement getChild(int index) {
        throw new UnsupportedOperationException("LA SEGUENTE UNITA NON PREVEDE LA PRESENZA DI FIGLI");
    }

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

}
