package command;

import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import mediator.ChangeManagerMediator;

public class DisegnaElementCommand implements Command {
    private final PannelloDisegno pd;
    private final ChangeManagerMediator mediator;
    private OrganigrammaElement element;

    public DisegnaElementCommand(PannelloDisegno p, ChangeManagerMediator mediator,OrganigrammaElement element) {
        this.pd = p;
        this.mediator=mediator;
        this.element = element;
    }

    @Override
    public boolean doIt(){
        pd.aggiungiUnita(element);
        return true;
    }//supporto agilmente undo e redo


    @Override
    public boolean undoIt(){
        pd.rimossoFiglio(element);//qua a differenza chiamo rimosso figlio perche è quella che uso quando
        //elimino organo di gestione, quindi non devo trovare un padre da cui eliminare questo elemento come con gli altri
        return true;
    }


}
