package dycmaster.rysiek.actions;

import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.FILE_DATA_PROVIDERS;
import dycmaster.rysiek.deployment.FileDataProvider;
import dycmaster.rysiek.sensors.DummySensorListener;
import dycmaster.rysiek.sensors.FileSensor;
import dycmaster.rysiek.sensors.Sensor;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.triggers.DummySensorTrigger;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;



public class FileSensorTest {

	public static final URL TestSensorFile = FILE_DATA_PROVIDERS.class.getClassLoader().getResource("testSensor");

	private FileDataProvider deployTestScriptAndRun(){
		FileDataProvider testScript = new FileDataProvider(TestSensorFile);
		Collection<FileDataProvider> scripts = new LinkedList<>();
		scripts.add(testScript);
		DeploymentManager deploymentManager = new DeploymentManager();
		deploymentManager.deployAllAndRun(scripts);
		return  testScript;
	}

	@Test
	public void testStartAndStopObserving() throws Exception {
		FileDataProvider testScript = deployTestScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();
		Thread.sleep(20000);
		fs.stopObserving();
	}

	@Test
	public void testSensorListener () throws Exception {
		FileDataProvider testScript = deployTestScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new DummySensorListener(sensor);
		sensorListener.startListening();
		Thread.sleep(6000);
	}

	@Test
	public void testSensorTrigger () throws Exception {
		FileDataProvider testScript = deployTestScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new DummySensorTrigger(sensor);
		sensorListener.startListening();
		Thread.sleep(6000);
	}



}
