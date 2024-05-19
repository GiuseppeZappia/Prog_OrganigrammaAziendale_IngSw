package Utilities;

import java.util.Objects;

public class Ruolo {

    private String nome,descrizione,requisiti;
    private int stipendio;

    public Ruolo(String nome, String descrizione,String requisiti, int stipendio) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.requisiti = requisiti;
        this.stipendio = stipendio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruolo ruolo = (Ruolo) o;
        return stipendio == ruolo.stipendio &&
                Objects.equals(nome, ruolo.nome) &&
                Objects.equals(descrizione, ruolo.descrizione) &&
                Objects.equals(requisiti, ruolo.requisiti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, descrizione, requisiti, stipendio);
    }

    @Override
    public String toString() {
        return "Ruolo{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", requisiti='" + requisiti + '\'' +
                ", stipendio=" + stipendio +
                '}';
    }


}
