package Composite;

import Exceptions.FiglioUnitaNonValidoException;
import Mediator.ChangeManagerMediator;
import Observer.CambiamentoUnitaListener;

import java.util.Iterator;

public class UnitaOrganizzativa extends AbstractCompositeElementOrganigramma{
    private String nome;
    private ChangeManagerMediator mediatore;

    public UnitaOrganizzativa(String nome,ChangeManagerMediator mediatore) {
        this.nome = nome;
        this.mediatore = mediatore;
    }

    public String getNome() {
        return nome;
    }

    @Override//protected perche è factory???
    protected ChangeManagerMediator creaMediatore() {
        return this.mediatore;
    }

    @Override
    public boolean addChild(OrganigrammaElement element) throws FiglioUnitaNonValidoException {
        if(!(element instanceof SottoUnitaOrganizzativa)){
            throw new FiglioUnitaNonValidoException();
        }
        boolean inserimento=super.addChild(element);
        super.notifyAddedChild(this,element);
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
    public boolean removeChild(OrganigrammaElement daEliminare) {
        Iterator<OrganigrammaElement> it= this.iterator();
        while(it.hasNext()){
            OrganigrammaElement elem= it.next();
            if(elem.equals(daEliminare)){
                it.remove();
                super.notifyRemovedChild(this,elem);
                return true;
            }
        }
        return false;
    }
}
