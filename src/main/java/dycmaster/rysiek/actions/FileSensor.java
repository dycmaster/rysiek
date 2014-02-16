package dycmaster.rysiek.actions;


import dycmaster.rysiek.deployment.FileSensorScript;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import java.io.File;

public class FileSensor extends  Sensor {

    File fileToObserve;
    File signalFile;

    public FileSensor(FileSensorScript script){
        this.fileToObserve = script.getTargetFile();
        this.signalFile = script.getSignalFile();

    }

    public FileSensor(File fileToObserve, File signalFile){
        this.fileToObserve = fileToObserve;
        this.signalFile = signalFile;
    }


    @Override
    public void startObserving() {

        int mask = JNotify.FILE_MODIFIED;
        try {
            int watchId = JNotify.addWatch(fileToObserve.getPath(),mask,false, changeListener);
        } catch (JNotifyException e) {
            e.printStackTrace();
        }
    }

    public void stopObserving(){

    }


    JNotifyListener changeListener = new JNotifyListener() {
        @Override
        public void fileCreated(int i, String s, String s2) {

        }

        @Override
        public void fileDeleted(int i, String s, String s2) {

        }

        @Override
        public void fileModified(int i, String s, String s2) {

            if(didStateChange()){
                stateHasChanged();
            }
        }

        protected  boolean didStateChange(){
            return  true;
        }

        @Override
        public void fileRenamed(int i, String s, String s2, String s3) {

        }
    };



}
