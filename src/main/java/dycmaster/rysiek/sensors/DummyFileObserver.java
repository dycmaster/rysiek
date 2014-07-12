package dycmaster.rysiek.sensors;


import java.util.Timer;
import java.util.TimerTask;

public class DummyFileObserver extends  AbstractFileObserver {

    private Timer timer = new Timer();
    private boolean isOn = false;

    @Override
    public void startObserving() {
        timer.scheduleAtFixedRate(notifyTask, 100, 100);
        isOn = true;
    }

    @Override
    public void stopObserving() {
        timer.cancel();
        isOn = false;
    }

    @Override
    public boolean isObserving() {
        return isOn;
    }

    TimerTask notifyTask = new TimerTask() {
        @Override
        public void run() {
            fireEvent();
        }
    };

    private void fireEvent(){
        FileObserverEventArgs args = new FileObserverEventArgs();
        notifyObservers(args);
    }
}
