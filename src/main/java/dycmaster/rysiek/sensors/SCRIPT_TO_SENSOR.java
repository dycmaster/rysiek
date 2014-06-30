package dycmaster.rysiek.sensors;

import dycmaster.rysiek.sensors.sensorParsers.MusicOnSensorParser;
import dycmaster.rysiek.sensors.sensorParsers.SensorParser;
import dycmaster.rysiek.sensors.sensorParsers.TempSensorParser;


public enum SCRIPT_TO_SENSOR {


	soundCardActivity("soundCardActivity", "MusicOnSensorParser", MusicOnSensorParser.class),
	tempSensor("tempShutdown", "TempSensorParser", TempSensorParser.class);



	private  String scriptName;

	public String getSensorName() {
		return sensorName;
	}

	public String getScriptName() {
		return scriptName;
	}

	public SensorParser getParser() {
		return parser;
	}

	public Class getSensorClass() {
		return sensorClass;
	}

	private  String sensorName;
	private SensorParser parser;
	private Class sensorClass;

	private SCRIPT_TO_SENSOR(String scriptName, String sensorConfigName){
		this.scriptName = scriptName;
		this.sensorName = sensorConfigName;
		try{
			Class<?> clazz = Class.forName(sensorConfigName);
			parser = (SensorParser) clazz.newInstance();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private SCRIPT_TO_SENSOR(String scriptName, String sensorConfigName, Class sensorClass){
		this(scriptName, sensorConfigName);
		this.sensorClass = sensorClass;
	}

	private SCRIPT_TO_SENSOR(String scriptName, String sensorConfigName, String sensorClass){
		this(scriptName, sensorConfigName);
		try {
			this.sensorClass = Class.forName(sensorClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
