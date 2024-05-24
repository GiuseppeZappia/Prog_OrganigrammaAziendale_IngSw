package Command;

import Exceptions.FiglioNonPresenteInQuestaUnitaException;
import Exceptions.SubjectSenzaListenerInAscoltoException;

public interface Command {

    boolean disegna();

    boolean rimuovi() ;
}
