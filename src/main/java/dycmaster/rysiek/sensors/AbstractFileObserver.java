package dycmaster.rysiek.sensors;


import dycmaster.rysiek.shared.Create;

import java.io.File;
import java.util.Collection;

public abstract class AbstractFileObserver implements  FileObserver {

    private final File observedFile;

    public AbstractFileObserver(File file){
        observedFile = file;
    }



    public String getObservedFileName(){
        if(observedFile!=null){
            return observedFile.getName();
        }else{
            return "";
        }
    }


    protected  File getObservedFile(){
        return  observedFile;
    }

    private final  Collection<FileObserverSubscriber> subscribers = Create.newCollection();

    private void addSubscriber(FileObserverSubscriber subscriber ){
        subscribers.add(subscriber);
    }

    @Override
    public abstract void startObserving();

    @Override
    public abstract void stopObserving();

    @Override
    public void subscribeToObserver(FileObserverSubscriber subscriber) {
        addSubscriber(subscriber);
    }

    protected void notifyObservers(FileObserverEventArgs args){
        synchronized(subscribers){
            for(FileObserverSubscriber subscriber: subscribers){
                subscriber.handleFileObserverEvent(args);
            }
        }
    }


}
