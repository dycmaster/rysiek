package dycmaster.rysiek.triggers;

import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.sensors.SensorValue;
import dycmaster.rysiek.triggers.triggerParsers.TriggerParametersParser;
import dycmaster.rysiek.triggers.triggerParsers.flipFlopTriggerParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;


public class SensorTriggerMockedTestMockito {

	@Mock private SensorListener sensorListener;
	@Mock TriggerListener triggerListener;
	@Mock SensorValue sensorValue;
	@Mock Map<String,String> triggerParameters;

	@Test
	public void testSensorTriggerFlipFlopParser() throws  Exception{

		TriggerParametersParser flipFlop = new flipFlopTriggerParser();
		boolean initState = flipFlop.parseParametersToState(triggerParameters);
		boolean secondState = flipFlop.parseParametersToState(triggerParameters);

		Assert.assertNotEquals(initState, secondState);
	}

	@Test
	public void testSensorTriggerFiresAnEvent() throws Exception{
		//prepare objects
		Trigger testTrigger = new SensorTrigger(sensorListener, new flipFlopTriggerParser());
		testTrigger.subscribeToTrigger(triggerListener);
		testTrigger.start();

		//record init state
		boolean initState = testTrigger.getTriggerState();

		//change trigger state
		testTrigger.setTriggerState(!initState);

		//Verify the callback was called
		Mockito.verify(triggerListener).triggerValueChangedHandler(Mockito.any(TriggerValue.class));
	}


}
