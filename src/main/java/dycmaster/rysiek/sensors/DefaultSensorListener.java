package dycmaster.rysiek.sensors;


import dycmaster.rysiek.sensors.sensorParsers.SensorParser;
import dycmaster.rysiek.shared.Create;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DefaultSensorListener implements SensorListener {

    private boolean isListening;
    private Date lastChangeTime;
    private volatile Map<String, String> _parsedParameters = new HashMap<>();
    private SensorParser sensorParser;

    private volatile Collection<SensorInterpreter> _subscribers =
            CollectionUtils.synchronizedCollection(Create.<SensorInterpreter>newCollection());

    public DefaultSensorListener(){
    }
    public DefaultSensorListener(SensorParser sensorParser) {
        setSensorParser(sensorParser);
    }

    public Collection<SensorInterpreter> getSubscribers() {
        return _subscribers;
    }

    @Override
    public void removeSubscriber(SensorInterpreter interpreter) {
        _subscribers.remove(interpreter);
    }

    @Override
    public void addSubscriber(SensorInterpreter interpreter) {
        _subscribers.add(interpreter);
    }

    protected void sensorParametersChangedEvent(Map<String, String> parameters) {
        synchronized (this) {
            for (SensorInterpreter interpreter : getSubscribers()) {
                interpreter.sensorParametersChangedHandler(parameters);
            }
        }
    }

    public SensorParser getSensorParser() {
        return sensorParser;
    }

    @Override
    public void setSensorParser(SensorParser sensorParser) {
        this.sensorParser = sensorParser;
    }

    @Override
    public Map<String, String> getParsedParameters() {
        synchronized (this) {
            return _parsedParameters;
        }
    }

    @Override
    public Date getLastChangeTime() {
        return lastChangeTime;
    }


    /*
     * Called by the Sensor which generates some event.
     * Inside the data is being parsed.
     * @param sensorValue
     */
    @Override
    public void sensorValueChangedHandler(SensorValue sensorValue) {
        if(isListening) {
            synchronized (this) {
                _parsedParameters = parseSensorValueToParameters(sensorValue, getSensorParser());
            }
            this.lastChangeTime = new Date();
            sensorParametersChangedEvent(_parsedParameters);
        }
    }

    /*
     * In this method you have to extract parameter values from sensed value
     */
    protected Map<String, String> parseSensorValueToParameters(SensorValue sensorValue, SensorParser parser) {
        return parser.parse(sensorValue);
    }

    @Override
    public void startListening() {
        isListening = true;
    }

    @Override
    public void stopListening() {
        isListening = false;
    }

    @Override
    public boolean isEnabled() {
        return isListening;
    }

    @Override
    public Collection<String> getAvailableParameters() {
        return sensorParser.getAvailableParameters();
    }
}
