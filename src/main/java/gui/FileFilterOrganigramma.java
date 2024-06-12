package gui;


import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileFilterOrganigramma extends FileFilter {
    @Override
    public boolean accept(File f) {//quali file utente vede tra selezionabili
        if(f.isDirectory()){
            return true;//senno non puo navigare tra cartelle
        }
        //devo capire quali file terminano per .orgaz e farli vedere
        String estensione= Utils.getExtension(f);
        if(estensione==null){
            return false;
        }
        return estensione.equals("orgaz");
    }

    @Override
    public String getDescription() {//descrizione visualizzata nel riquadro, quindi quelli selzionabili
        return "File Organigramma Aziendale (*.orgaz)";
    }
}
