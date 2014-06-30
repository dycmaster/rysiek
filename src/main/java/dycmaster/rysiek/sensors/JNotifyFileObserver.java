package dycmaster.rysiek.sensors;


import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;
import org.apache.log4j.Logger;

import java.io.File;


public class JNotifyFileObserver extends AbstractFileObserver {

    private int fileWatchId;
    private boolean _isEnabled;
    private Logger logger = Logger.getLogger(JNotifyFileObserver.class.getSimpleName() + ":" + getObservedFileName() );

    public JNotifyFileObserver(File fileToObserve){
        super(fileToObserve);
    }



    @Override
    public void startObserving() {
        try {
            int mask = JNotify.FILE_MODIFIED;
            fileWatchId = JNotify.addWatch(getObservedFile().getPath(), mask, false, changeListener);
            _isEnabled = true;
        } catch (JNotifyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopObserving() {
        boolean res = false;
        try {
            res = JNotify.removeWatch(fileWatchId);
            _isEnabled = false;
        } catch (JNotifyException e) {
            e.printStackTrace();
            logger.info(getObservedFileName()+ " observer: exception while removing watcher!");
        }
        if (res) {
            logger.info(getObservedFileName() + " observer: watcher succesfully removed.");
        } else {
            logger.info(getObservedFileName() + " observer: invalid watcher ID specified..");
        }
    }

    @Override
    public boolean isObserving() {
        return _isEnabled;
    }

    protected JNotifyListener changeListener = new JNotifyListener() {
        @Override
        public void fileCreated(int i, String s, String s2) {
        }

        @Override
        public void fileDeleted(int i, String s, String s2) {
        }

        @Override
        public void fileModified(int wd, String rootPath, String name) {
            FileObserverEventArgs args = new FileObserverEventArgs();
            notifyObservers(args);
        }

        @Override
        public void fileRenamed(int i, String s, String s2, String s3) {

        }
    };
}
