package observer;

import composite.OrganigrammaElement;

public interface CambiamentoUnitaListener {
    void rimossoFiglio(OrganigrammaElement figlio);
    void inseritoFiglio(OrganigrammaElement figlio);

}
