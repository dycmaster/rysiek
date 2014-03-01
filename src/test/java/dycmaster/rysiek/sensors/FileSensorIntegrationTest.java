package dycmaster.rysiek.sensors;

import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import dycmaster.rysiek.sensors.sensorParsers.FlipFlopSensorParser;
import dycmaster.rysiek.triggers.SensorTrigger;
import dycmaster.rysiek.triggers.Trigger;
import dycmaster.rysiek.triggers.triggerParsers.flipFlopTriggerParser;
import org.junit.Test;
import static org.mockito.Mockito.*;


import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class FileSensorIntegrationTest {

	public static final URL testFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");


	@Test
	public void testSensorListenerNotifies () throws Exception {
		ScriptRunner testScript = deployTestScriptAndRun();

		Sensor sensor = new FileSensor(testScript);
		sensor.start();

		SensorListener sensorListener = spy( new DefaultSensorListener(sensor, new FlipFlopSensorParser()));
		sensorListener.start();

		Thread.sleep(3000);

		verify(sensorListener, atLeastOnce()).sensorValueChangedHandler(any(SensorValue.class));
	}



	private ScriptRunner deployTestScriptAndRun(){
		ScriptRunner testScript = new ScriptRunner(testFileDataProvider);
		Collection<ScriptRunner> scripts = new LinkedList<>();
		scripts.add(testScript);
		DeploymentManager deploymentManager = new DeploymentManager();
		deploymentManager.deployAllAndRun(scripts);
		return  testScript;
	}



}
