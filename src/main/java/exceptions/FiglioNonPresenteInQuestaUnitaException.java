package exceptions;

public class FiglioNonPresenteInQuestaUnitaException extends RuntimeException {
    public FiglioNonPresenteInQuestaUnitaException() {
        super("Figlio non presente in questa unita ");
    }
}
