package Utilities;

import Composite.OrganigrammaElement;
import Exceptions.DipendenteGiaEsistenteException;
import Exceptions.DipendenteNonPresenteNellUnitaException;

import java.util.Collection;
import java.util.LinkedList;

public class Dipendente {
    private OrganigrammaElement unitaDiCuiFaParte;
    private Ruolo ruolo;

    public Ruolo getRuolo(){
        return null;
    }

    public void setRuolo(Ruolo ruolo){
        if(!unitaDiCuiFaParte.getRuoliDisponibili().contains(ruolo)){
            System.out.println("QUEL RUOLO NON È DISPONIBILE NELL'UNITA'");//fai eccezione dopo
        }
        this.ruolo=ruolo;
    }

    public boolean aggiungiDipendente() throws DipendenteGiaEsistenteException {
        return unitaDiCuiFaParte.addDipendenti(this);
    }


    //QUESTO VERRA CHIAMATO DALLA GUI CHE È STATA AVVISATA GRAZIE AD OBSERVER DEL FATTO CHE UN RUOLO È STATO ELIMINATO
    //QUINDI SE VIENE CHIAMATO QUESTO VUOL DIRE CHE IL MIO UTENTE AVEVA QUEL RUOLO E GLIELO DEVO CAMBIARE, RICEVE IL RUOLO SCRITTO DALL'UTENTE
    //MAGARI MEGLIO PASSARE SOLO LA STRINGA DEL NOME RUOLO E POI ASSEGNARGLIELO
    public boolean cambiaRuolo(Ruolo nuovo) throws DipendenteNonPresenteNellUnitaException {
        this.setRuolo(nuovo);
        return  unitaDiCuiFaParte.changeRuoloToDipendente(this);
    }


}
