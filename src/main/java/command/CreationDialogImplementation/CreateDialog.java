package command.CreationDialogImplementation;

import composite.OrganigrammaElement;
import gui.PannelloDisegno;

import javax.swing.*;

public interface CreateDialog {

    JDialog createDialog(PannelloDisegno pd, OrganigrammaElement elem);

}
