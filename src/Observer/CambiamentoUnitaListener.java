package Observer;

import Composite.OrganigrammaElement;
import Utilities.Dipendente;
import Utilities.Ruolo;

import java.util.LinkedList;

public interface CambiamentoUnitaListener {
    void ruoloCambiato(Ruolo r, LinkedList<Dipendente> dipententi);
    void rimossoFiglio(OrganigrammaElement padre,OrganigrammaElement figlio);
    void inseritoFiglio(OrganigrammaElement padre, OrganigrammaElement figlio);
}
