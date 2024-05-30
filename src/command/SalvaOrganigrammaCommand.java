package command;

import gui.PannelloDisegno;

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

//            OrganigrammaElement[] array = pd.getUnitaDisegnate().toArray(new OrganigrammaElement[pd.getUnitaDisegnate().size()]);
            oos.writeObject(pd.creaMemento());

            oos.close();
            fos.close();
        }catch(IOException e){
            //JOptionPane.showMessageDialog(pd,"IMPOSSIBILE SALVARE","Errore",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
