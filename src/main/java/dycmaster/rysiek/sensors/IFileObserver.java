package dycmaster.rysiek.sensors;


import java.io.File;

public interface IFileObserver {

    public void startObserving();
    public void stopObserving();
    public boolean isObserving();
    public void subscribeToObserver(FileObserverSubscriber subscriber);
    public void setObservedFile(File observedFile);

}
