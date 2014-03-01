package dycmaster.rysiek.triggers;

import dycmaster.rysiek.BaseTestMockito;
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

import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class SensorTriggerIntegrationTestMockito extends BaseTestMockito {

	public static final URL testFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");

	private ScriptRunner deployTestScriptAndRun(){
		ScriptRunner testScript = new ScriptRunner(testFileDataProvider);
		Collection<ScriptRunner> scripts = new LinkedList<>();
		scripts.add(testScript);
		DeploymentManager deploymentManager = new DeploymentManager();
		deploymentManager.deployAllAndRun(scripts);
		return  testScript;
	}

	@Test
	public void testFromRunningScriptToTriggerEvents() throws Exception {
		ScriptRunner testScript = deployTestScriptAndRun();
		Sensor sensor = new FileSensor(testScript);
		sensor.start();

		SensorListener sensorListener = new DefaultSensorListener(sensor, new MusicOnSensorParser());
		sensorListener.start();

		Trigger trigger = new SensorTrigger(sensorListener, new flipFlopTriggerParser());
		trigger.start();

		TriggerListener triggerListener = new AbstractTriggerListener() {
			@Override
			protected void triggerValueChangedAction(TriggerValue triggerValue) {

			}
		};
		TriggerListener spy = spy(triggerListener);
		trigger.subscribeToTrigger(spy);

		Thread.sleep(3000);

		verify(spy, atLeastOnce()).triggerValueChangedHandler(any(TriggerValue.class));
	}
}
