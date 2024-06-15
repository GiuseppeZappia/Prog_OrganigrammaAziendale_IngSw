package exceptions;

public class DipendenteGiaEsistenteException extends RuntimeException {
    public DipendenteGiaEsistenteException() {
        super("DIPENDENTE GIA ESISTENTE");
    }
}
