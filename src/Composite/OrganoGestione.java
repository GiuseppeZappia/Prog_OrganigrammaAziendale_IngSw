package Composite;

import Exceptions.FiglioUnitaNonValidoException;
import Mediator.ChangeManagerMediator;
import Observer.CambiamentoUnitaListener;

public class OrganoGestione extends AbstractCompositeElementOrganigramma{
    private String nome;
    private ChangeManagerMediator mediatore;

    public OrganoGestione(String nome,ChangeManagerMediator mediatore) {
        this.nome = nome;
        this.mediatore = mediatore;
        this.elements.add(this);//MI INSERISCO NELLA LISTA COSI SE MI VOGLIO ELIMINARE LO POSSO FARE CON UNA FUNZIONE
                                //CHE MI BASTA INSERIRE DOVE ELIMINO SIA ME CHE I FIGLI E INVIOO TUTTO AL LISTENER
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
        if(!(element instanceof UnitaOrganizzativa)){
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

    public void rimuoviTutto(){
        //METODO CHE CANCELLA TUTTO ORGANIRGAMMA, VISTO CHE VOGLIO ELIMINARE TESTA, MI BASTA SVUOTARE LA LISTA ELEMENTS
        //PERCHE TANTO NELLA GUI IMPLEMENTO DISEGNO SCORRENDO ELEMENTI PRESENTI IN QUELLA E FACENDOLI DISEGNARE??
    }
}
