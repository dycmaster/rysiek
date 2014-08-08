package dycmaster.rysiek.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;


public class FileTools {

    private static final Logger log = LoggerFactory.getLogger(FileTools.class);

    public static File openFile(String path) {
        try {
            return new File(path);
        } catch (Exception e) {
            log.error("exception while opening file!", e);
            return null;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFoldersContent(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFoldersContent(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
