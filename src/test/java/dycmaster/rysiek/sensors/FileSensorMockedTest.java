package dycmaster.rysiek.sensors;

import dycmaster.rysiek.MockitoTest;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


public class FileSensorMockedTest extends MockitoTest {

	@Test
	public void testFileSensorNotifiesAboutAChange(){

		//objects and values
		Sensor testSensor = new FileSensor();
		Sensor spySensor  = Mockito.spy(testSensor);
		List<String> sensorValList = new ArrayList<>();
		sensorValList.add("test success");
		SensorValue inputSV = new SensorValue(sensorValList);

		//stub
		Mockito.doReturn(inputSV).when(spySensor).getChangedEventArgs();

		//test
		spySensor.onChangeDetected();

		//trust and verify
		Mockito.verify(spySensor).changed(inputSV);
	}
}
