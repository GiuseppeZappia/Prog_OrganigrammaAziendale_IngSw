package exceptions;

public class RuoloNonPresenteNellUnitaException extends RuntimeException {
    public RuoloNonPresenteNellUnitaException() {
        super("RUOLO NON PRESENTE NELL'UNITA'");
    }
}
