package dycmaster.rysiek.triggers;

import dycmaster.rysiek.MockitoTest;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.triggers.triggerParsers.flipFlopTriggerParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


public class SensorTriggerMockedTest  extends MockitoTest{

	@Mock
	private SensorListener sensorListener;

	@Test
	public void testSensorTriggerFlipFlopParser() throws  Exception{
		//prepare objects
		Trigger testTrigger = new SensorTrigger(sensorListener, new flipFlopTriggerParser());
		Trigger testTriggerSpy = Mockito.spy(testTrigger);
		testTriggerSpy.setEnabled(true);

		//record init state
		boolean initState = testTriggerSpy.getTriggerState();

		//test and wait for the second thread
		testTriggerSpy.triggerInputChanged();
		Thread.sleep(500);

		//trust and verify..
		boolean afterState = testTriggerSpy.getTriggerState();
		Assert.assertTrue(initState != afterState);
	}


}
