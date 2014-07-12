package dycmaster.rysiek.triggers;

import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class SensorTriggerIntegrationTestMockito {

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
//		ScriptRunner testScript = deployTestScriptAndRun();
//		Sensor sensor = new FileSensor(testScript);
//		sensor.startObserving();
//
//		SensorListener sensorListener = new DefaultSensorListener(sensor, new MusicOnSensorParser());
//		sensorListener.startListening();
//
//		Trigger trigger = new SensorTrigger(sensorListener, new flipFlopTriggerParser());
//		trigger.startListening();
//
//		TriggerListener triggerListener = new AbstractTriggerListener() {
//			@Override
//			protected void triggerValueChangedAction(TriggerValue triggerValue) {
//
//			}
//		};
//		TriggerListener spy = spy(triggerListener);
//		trigger.subscribeToTrigger(spy);
//
//		Thread.sleep(3000);
//
//		verify(spy, atLeastOnce()).triggerValueChangedHandler(any(TriggerValue.class));
	}
}
