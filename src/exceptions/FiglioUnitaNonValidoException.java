package exceptions;

public class FiglioUnitaNonValidoException extends Exception {
    public FiglioUnitaNonValidoException() {
        super("IL FIGLIO PASSATO NON È ACCETTATO PER LA SEGUENTE UNITA'");
    }
}
