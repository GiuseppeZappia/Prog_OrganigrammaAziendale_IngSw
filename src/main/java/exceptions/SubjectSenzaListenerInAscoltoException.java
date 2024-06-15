package exceptions;

public class SubjectSenzaListenerInAscoltoException extends RuntimeException {
    public SubjectSenzaListenerInAscoltoException() {
        super("NESSUN LISTENER IN ASCOLTO PER QUEL SUBJECT");
    }
}
