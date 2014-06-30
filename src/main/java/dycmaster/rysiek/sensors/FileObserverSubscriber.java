package dycmaster.rysiek.sensors;


public interface FileObserverSubscriber {
    public void handleFileObserverEvent(FileObserverEventArgs args);
}
