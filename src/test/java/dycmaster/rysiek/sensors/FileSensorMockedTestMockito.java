package dycmaster.rysiek.sensors;

import dycmaster.rysiek.BaseTestMockito;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;


public class FileSensorMockedTestMockito extends BaseTestMockito {

	@Test
	public void testFileSensorNotifiesUponChanges() throws Exception{

		Sensor sensor =  spy(new FileSensor());
		sensor.onChangeDetected();
		verify(sensor).observedEntityChangedEvent(any(SensorValue.class));
	}

	@Test
	public void testFileSensorNotifiesAboutAChange(){

		//objects and values
		Sensor testSensor = new FileSensor();
		Sensor spySensor  = Mockito.spy(testSensor);
		List<String> sensorValList = new ArrayList<>();
		sensorValList.add("test success");
		SensorValue inputSV = new SensorValue(sensorValList);

		//stub
		doReturn(inputSV).when(spySensor).getChangedEventArgs();

		//test
		spySensor.onChangeDetected();

		//trust and verify
		verify(spySensor).observedEntityChangedEvent(inputSV);
	}
}
