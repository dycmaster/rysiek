package dycmaster.rysiek.sensors;


public interface FileObserver {

    public void startObserving();
    public void stopObserving();
    public boolean isObserving();
    public void subscribeToObserver(FileObserverSubscriber subscriber);

}
