package composite.utilities;

import java.io.Serializable;
import java.util.Objects;

public class Ruolo implements Serializable {

    private String nome,descrizione,requisiti;
    private Number stipendio;
    private static final long serialVersionUID = 1L;

    public Ruolo(String nome, String descrizione,String requisiti, Number stipendio) {
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
        return
//                Objects.equals(stipendio,ruolo.stipendio) &&
                Objects.equals(nome, ruolo.nome) ;
//                Objects.equals(descrizione, ruolo.descrizione) &&
//                Objects.equals(requisiti, ruolo.requisiti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, descrizione, requisiti, stipendio);
    }

    @Override
    public String toString() {
        return   nome +": "+ "\n" +
                " descrizione=" + descrizione +" "+ "\n" +
                ", requisiti=" + requisiti +" "+ "\n" +
                ", stipendio=" + stipendio ;
    }

    public String getNome() {
        return nome;
    }

}
