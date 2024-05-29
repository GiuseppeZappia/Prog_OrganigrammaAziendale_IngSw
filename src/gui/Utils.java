package gui;

import java.io.File;


//dalla documetazione vedo che va creata questa classe di utilita con questo metodo per prendere cosi estensione facendo semplice parsing dei nomi
public class Utils {

    public static String getExtension(File f){
        String extension = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            extension = s.substring(i+1).toLowerCase();
        }
        return extension;
    }

}
