package exceptions;

public class SubjectSenzaListenerInAscoltoException extends Exception {
    public SubjectSenzaListenerInAscoltoException() {
        super("NESSUN LISTENER IN ASCOLTO PER QUEL SUBJECT");
    }
}
