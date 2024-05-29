package observer;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observer;

public interface CambiamentoUnitaListener {
//    void ruoloCambiato(Ruolo r, LinkedList<Dipendente> dipententi);
    void rimossoFiglio(OrganigrammaElement figlio);
    void inseritoFiglio(OrganigrammaElement padre, OrganigrammaElement figlio);

}
