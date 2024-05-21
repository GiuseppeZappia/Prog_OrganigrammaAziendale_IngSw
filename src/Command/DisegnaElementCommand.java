package Command;

import Composite.OrganigrammaElement;
import GUI.PannelloDisegno;

public class DisegnaElementCommand implements Command {
    private final PannelloDisegno pannelloDisegno;
    private final OrganigrammaElement element;

    public DisegnaElementCommand(PannelloDisegno p, OrganigrammaElement o) {
        this.pannelloDisegno = p;
        this.element = o;
    }

    @Override
    public boolean disegna(){
        pannelloDisegno.aggiungiUnita(element);
        return true;
    }

    @Override
    public boolean rimuovi(){
        pannelloDisegno.rimuoviUnita(element);
        return true;
    }


}
