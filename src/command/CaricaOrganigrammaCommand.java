package command;

import composite.OrganigrammaElement;
import gui.PannelloDisegno;
import memento.PannelloDisegnoMemento;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class CaricaOrganigrammaCommand implements Command{
    private File file;
    private PannelloDisegno pd;

    public CaricaOrganigrammaCommand(File file ,PannelloDisegno pd) {
        this.file = file;
        this.pd = pd;
    }


    @Override
    public boolean doIt() throws IOException {
            FileInputStream fis=new FileInputStream(file);
            ObjectInputStream ois=new ObjectInputStream(fis);
            OrganigrammaElement[] array;
            try {
//                array = (OrganigrammaElement[]) ois.readObject();
                PannelloDisegnoMemento memento = (PannelloDisegnoMemento) ois.readObject();
//                pd.getUnitaDisegnate().clear();
                pd.ripristinaMementoPerCaricareDaFile(memento);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(pd,"IMPOSSIBILE SALVARE","Errore",JOptionPane.ERROR_MESSAGE);
            }
//            pd.salva(array);

            ois.close();
            fis.close();
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
