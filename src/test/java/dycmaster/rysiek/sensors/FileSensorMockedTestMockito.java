package dycmaster.rysiek.sensors;

import dycmaster.rysiek.BaseContextTestTemplate;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;


public class FileSensorMockedTestMockito extends BaseContextTestTemplate {

    @Autowired
    FileSensor fileSensor;

	@Test
	public void testFileSensorNotifiesUponChanges() throws Exception{
        SensorListener listener = mock(SensorListener.class);
        fileSensor.subscribeToSensor(listener);
        fileSensor.onChangeDetected();
        Mockito.verify(listener, Mockito.times(1)).sensorValueChangedHandler(Mockito.any(SensorValue.class));
	}

	@Test
	public void testFileSensorNotifiesAboutAChange(){

		//objects and values

		Sensor spySensor  = Mockito.spy(fileSensor);
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
