package exceptions;

public class RuoloGiaPresenteNellUnitaException extends RuntimeException {
    public RuoloGiaPresenteNellUnitaException() {
        super("IL RUOLO INSERITO È GIA PRESENTE");
    }
}
