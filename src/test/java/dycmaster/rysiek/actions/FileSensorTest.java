package dycmaster.rysiek.actions;

import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.FILE_SENSORS;
import dycmaster.rysiek.deployment.FileSensorScript;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;



public class FileSensorTest {

	public static final URL TestSensorFile = FILE_SENSORS.class.getClassLoader().getResource("testSensor");

	private FileSensorScript deployTestScriptAndRun(){
		FileSensorScript testScript = new FileSensorScript(TestSensorFile);
		Collection<FileSensorScript> scripts = new LinkedList<>();
		scripts.add(testScript);
		DeploymentManager deploymentManager = new DeploymentManager();
		deploymentManager.deployAllAndRun(scripts);
		return  testScript;
	}

	@Test
	public void testStartAndStopObserving() throws Exception {
		FileSensorScript testScript = deployTestScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();
		Thread.sleep(6000);
		fs.stopObserving();
	}

	@Test
	public void testSensorListener () throws Exception {
		FileSensorScript testScript = deployTestScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();
		SensorListener sensorListener = new DefaultSensorListener(fs);
		sensorListener.startListening();
		Thread.sleep(6000);
		sensorListener.stopListening();
		fs.stopObserving();
	}


}
