package dycmaster.rysiek.sensors;

import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import dycmaster.rysiek.sensors.sensorParsers.FlipFlopSensorParser;
import dycmaster.rysiek.triggers.SensorTrigger;
import dycmaster.rysiek.triggers.Trigger;
import dycmaster.rysiek.triggers.triggerParsers.flipFlopTriggerParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class FileSensorIntegrationTest {

	public static final URL testFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");




//	@Test
//	public void testStartAndStopObserving() throws Exception {
//		ScriptRunner testScriptRunner = deployTestScriptAndRun();
//		Sensor s = new FileSensor(testScriptRunner);
//		s.startObserving();
//		Thread.sleep(2000);
//		Assert.assertNotNull("buu",s.getLastChangeTime());
//
//	}

	@Test
	public void testSensorListener () throws Exception {
		ScriptRunner testScript = deployTestScriptAndRun();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new DefaultSensorListener(sensor, new FlipFlopSensorParser());
		sensorListener.startListening();

		Thread.sleep(5000);
	}

	@Test
	public void testSensorTrigger () throws Exception {
		ScriptRunner testScript = deployTestScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new DefaultSensorListener(sensor, new FlipFlopSensorParser());
		sensorListener.startListening();

		Trigger trigger = new SensorTrigger(sensorListener, new flipFlopTriggerParser());
		trigger.setEnabled(true);

		Thread.sleep(4000);
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
