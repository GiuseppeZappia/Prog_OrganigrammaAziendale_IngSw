package observer;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;

import java.util.LinkedList;

public interface CambiamentoUnitaListener {
//    void ruoloCambiato(Ruolo r, LinkedList<Dipendente> dipententi);
    void rimossoFiglio(OrganigrammaElement figlio);
    void inseritoFiglio(OrganigrammaElement padre, OrganigrammaElement figlio);
}