package dycmaster.rysiek.sensors;


import dycmaster.rysiek.sensors.sensorParsers.SensorParser;
import org.apache.commons.collections.CollectionUtils;
import java.util.*;


public  class DefaultSensorListener implements SensorListener {

	private Sensor mySensor;
	private boolean isListening;
	private Date lastChangeTime;
	private volatile Map<String, String> _parsedParameters = new HashMap<>();
	private SensorParser sensorParser;

	private volatile Collection<SensorInterpreter> _subscribers = CollectionUtils.synchronizedCollection(new LinkedList());

	public Collection<SensorInterpreter> getSubscribers(){
		return  _subscribers;
	}

	@Override
	public void removeSubscriber(SensorInterpreter interpreter){
		_subscribers.remove(interpreter);
	}

	@Override
	public void addSubscriber(SensorInterpreter interpreter){
		_subscribers.add(interpreter);
	}


	protected void sensorParametersChangedEvent(Map<String, String> parameters){
		synchronized (this){
			for(SensorInterpreter interpreter: getSubscribers()){
				interpreter.sensorParametersChangedHandler(parameters);
			}
		}
	}


	public DefaultSensorListener(Sensor sensor, SensorParser sensorParser) {
		setSensor(sensor);
		setSensorParser(sensorParser);
	}


	public SensorParser getSensorParser() {
		return sensorParser;
	}

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

	@Override
	public Sensor getSensor() {
		return mySensor;
	}

	@Override
	public void setSensor(Sensor sensor) {
		mySensor = sensor;
	}

	/*
	 * Called by the Sensor which generates some event.
	 * Inside the data is being parsed.
	 * @param sensorValue
	 */
	@Override
	public void sensorValueChangedHandler(SensorValue sensorValue) {
		synchronized (this) {
			_parsedParameters = parseSensorValueToParameters(sensorValue, getSensorParser());
		}
		this.lastChangeTime = new Date();
		sensorParametersChangedEvent(_parsedParameters);
	}

	/*
	 * In this method you have to extract parameter values from sensed value
	 */
	protected Map<String, String> parseSensorValueToParameters(SensorValue sensorValue, SensorParser parser) {
		return parser.parse(sensorValue);
	}

	@Override
	public void start() {
		mySensor.subscribeToSensor(this);
		isListening = true;
	}

	@Override
	public void stop() {
		mySensor.removeSubscriber(this);
		isListening = false;
	}

	@Override
	public boolean isEnabled() {
		return isListening;
	}

	@Override
	public Collection<String> getAvailableParameters(){
		return  sensorParser.getAvailableParameters();
	}
}
