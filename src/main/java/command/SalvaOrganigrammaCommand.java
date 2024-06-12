package command;

import gui.PannelloDisegno;

import javax.swing.*;
import java.io.*;

public class SalvaOrganigrammaCommand implements Command{
    private File file;
    private PannelloDisegno pd;

    public SalvaOrganigrammaCommand(File file ,PannelloDisegno pd) {
        this.file = file;
        this.pd = pd;
    }


    @Override
    public boolean doIt()  {
        try {
            String percorso = file.getAbsolutePath();
            if (!percorso.endsWith(".orgaz")) {//se utente non inserisce estensione la inserisco io
                file = new File(percorso + ".orgaz");
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(pd.creaMemento());
            oos.close();
            fos.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(pd,"IMPOSSIBILE SALVARE","Errore",JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
