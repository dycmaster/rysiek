package dycmaster.rysiek.shared;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by frs on 2/15/14.
 */
public class FileTools {

    public static File openFile(URL fileUrl){
        try{
        File f = new File(fileUrl.toURI());
        return  f;
    }catch(Exception e){
        return  null;
    }
    }

    public static void deleteFoldersContent(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFoldersContent(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
