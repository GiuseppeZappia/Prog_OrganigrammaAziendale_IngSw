package composite.utilities;

import composite.OrganigrammaElement;
import exceptions.DipendenteNonPresenteNellUnitaException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;


public class Dipendente {
    //HO COSCIENZA DEL RUOLO CHE IL DIPENDENTE RICOPRE NELLE VARIE UNITA DOVE SI TROVA,
    //STO FACENDO IN MODO CHE IN UNA SINGOLA UNITA IL DIPENDENTE POSSA RICOPRIRE UN SOLO RUOLO
    private HashMap<OrganigrammaElement,Ruolo> unitaDiCuiFaParte=new HashMap<>();

    //fare in modo che per ogni dipendente possa vedere i RUOLI ricoperti
    private String nome,cognome,citta,indirizzo;
    private int eta;

    public Dipendente(String nome, String cognome, String citta, String indirizzo, int eta){
        this.nome = nome;
        this.cognome = cognome;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.eta = eta;
    }

    public void aggiungiDipendenteAdUnita(OrganigrammaElement unita,Ruolo ruolo){
        this.unitaDiCuiFaParte.put(unita,ruolo);
    }

    public void rimuoviDipendenteDaUnita(OrganigrammaElement unita){
        this.unitaDiCuiFaParte.remove(unita);
    }

    public HashMap<OrganigrammaElement, Ruolo> getUnitaDiCuiFaParte() {
        return unitaDiCuiFaParte;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCitta() {
        return citta;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Number getEta() {
        return eta;
    }

    public Collection<Ruolo> getRuoli(){
        return  unitaDiCuiFaParte.values();
    }

    public Ruolo getRuolo(OrganigrammaElement unita){
        return this.unitaDiCuiFaParte.get(unita);
    }

    public void setRuolo(OrganigrammaElement unita, Ruolo ruolo){
        if(unitaDiCuiFaParte.containsKey(unita) || !unita.getRuoliDisponibili().contains(ruolo)){
            System.out.println("ERRORE UNITA O QUEL RUOLO NON È DISPONIBILE NELL'UNITA'");//fai eccezione dopo
        }
        unitaDiCuiFaParte.put(unita,ruolo);//AGGIORNO RUOLO IN QUELLA UNITA DEL DIPENDENTE
    }

    //QUESTO VERRA CHIAMATO DALLA GUI CHE È STATA AVVISATA GRAZIE AD OBSERVER DEL FATTO CHE UN RUOLO È STATO ELIMINATO
    //QUINDI SE VIENE CHIAMATO QUESTO VUOL DIRE CHE IL MIO UTENTE AVEVA QUEL RUOLO E GLIELO DEVO CAMBIARE, RICEVE IL RUOLO SCRITTO DALL'UTENTE
    //MAGARI MEGLIO PASSARE SOLO LA STRINGA DEL NOME RUOLO E POI ASSEGNARGLIELO
    public boolean cambiaRuolo(Ruolo nuovo,OrganigrammaElement unita) throws DipendenteNonPresenteNellUnitaException {
        this.setRuolo(unita,nuovo);
        return  unita.changeRuoloToDipendente(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dipendente dipendente = (Dipendente) o;
        return eta==dipendente.eta &&
                nome.equals(dipendente.nome) &&
                cognome.equals(dipendente.cognome);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + cognome.hashCode();
        result = 31 * result + eta;
        return result;
    }

    @Override
    public String toString() {
        return "Dipendente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", citta='" + citta + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", eta=" + eta +
                '}';
    }

}