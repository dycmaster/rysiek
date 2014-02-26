package dycmaster.rysiek.triggers;

import dycmaster.rysiek.MockitoTest;
import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import dycmaster.rysiek.sensors.DefaultSensorListener;
import dycmaster.rysiek.sensors.FileSensor;
import dycmaster.rysiek.sensors.Sensor;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.sensors.sensorParsers.MusicOnSensorParser;
import dycmaster.rysiek.triggers.triggerParsers.flipFlopTriggerParser;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;


public class SensorTriggerIntegrationTest extends MockitoTest {

	private ScriptRunner deployMusicScriptAndRun() {
		ScriptRunner testScript = new ScriptRunner(SCRIPTS_TO_RUN.CheckSoundcard.getUrl());
		Collection<ScriptRunner> scripts = new LinkedList<>();
		scripts.add(testScript);
		DeploymentManager deploymentManager = new DeploymentManager();
		deploymentManager.deployAllAndRun(scripts);
		return testScript;
	}


	@Test
	public void testMusicSensorListener() throws Exception {
		ScriptRunner testScript = deployMusicScriptAndRun();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new DefaultSensorListener(sensor, new MusicOnSensorParser());
		sensorListener.startListening();

		Thread.sleep(20000);
	}


	@Test
	public void testMusicSensorDummyTrigger() throws Exception {
		ScriptRunner testScript = deployMusicScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new DefaultSensorListener(sensor, new MusicOnSensorParser());
		sensorListener.startListening();

		Trigger trigger = new SensorTrigger(sensorListener, new flipFlopTriggerParser());
		trigger.setEnabled(true);

		Thread.sleep(20000);
	}
}
