package dycmaster.rysiek.actions;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by frs on 2/16/14.
 */
public abstract class Sensor {

    private boolean state;
    List<SensorListener> sensorListeners = new LinkedList<>();

    public void subscribeToSensor(SensorListener listener){
        sensorListeners.add(listener);
    }

    protected  void stateHasChanged(){
        for(SensorListener listener: sensorListeners){
            listener.handleStateChanged(getState());
        }
    }


    public  boolean getState(){
        return state;
    }

    public void setState(boolean state){
        if(state != this.state){
            this.state = state;
            stateHasChanged();
        }
    }


    public abstract  void startObserving();

}
