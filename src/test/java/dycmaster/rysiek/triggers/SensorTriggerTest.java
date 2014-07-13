package dycmaster.rysiek.triggers;

import dycmaster.rysiek.BaseContextTestTemplate;
import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import dycmaster.rysiek.sensors.FileSensor;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.sensors.sensorParsers.EchoSensorParser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class SensorTriggerTest extends BaseContextTestTemplate {


    @Autowired
    FileSensor fileSensor;

    @Autowired
    SensorListener sensorListener;

	@Test
	public void testFromRunningScriptToTriggerEvents() throws Exception {

		fileSensor.addSubscriber(sensorListener);
        sensorListener.setSensorParser(new EchoSensorParser());
        fileSensor.startObserving();
        sensorListener.startListening();

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
