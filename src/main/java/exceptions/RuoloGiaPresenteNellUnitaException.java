package exceptions;

public class RuoloGiaPresenteNellUnitaException extends Exception {
    public RuoloGiaPresenteNellUnitaException() {
        super("IL RUOLO INSERITO È GIA PRESENTE");
    }
}
