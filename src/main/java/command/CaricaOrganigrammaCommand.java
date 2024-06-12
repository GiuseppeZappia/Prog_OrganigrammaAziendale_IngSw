package command;

import gui.PannelloDisegno;
import memento.Memento;
//import memento.PannelloDisegnoMemento;

import javax.swing.*;
import java.io.*;

public class CaricaOrganigrammaCommand implements Command {
    private File file;
    private PannelloDisegno pd;

    public CaricaOrganigrammaCommand(File file, PannelloDisegno pd) {
        this.file = file;
        this.pd = pd;
    }


    @Override
    public boolean doIt() throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            Memento memento = (Memento) ois.readObject();
            pd.ripristinaMemento(memento);
        } catch (EOFException e) {
            JOptionPane.showMessageDialog(pd, "Il file è vuoto o danneggiato", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(pd, "IMPOSSIBILE CARICARE", "Errore", JOptionPane.ERROR_MESSAGE);
        }
        ois.close();
        fis.close();
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
