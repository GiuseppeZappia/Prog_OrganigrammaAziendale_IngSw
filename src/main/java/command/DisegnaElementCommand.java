package command;

import composite.OrganigrammaElement;
import gui.PannelloDisegno;

public class DisegnaElementCommand implements Command {
    private final PannelloDisegno pd;
    private OrganigrammaElement element;

    public DisegnaElementCommand(PannelloDisegno p,OrganigrammaElement element) {
        this.pd = p;
        this.element = element;
    }

    @Override
    public boolean doIt(){
        pd.inseritoFiglio(element);
        return true;
    }//supporto agilmente undo e redo


    @Override
    public boolean undoIt(){
        pd.rimossoFiglio(element);
        //elimino organo di gestione, quindi non devo trovare un padre da cui eliminare questo elemento come con gli altri
        return true;
    }
}
