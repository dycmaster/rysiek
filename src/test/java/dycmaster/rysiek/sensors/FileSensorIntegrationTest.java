package dycmaster.rysiek.sensors;

import dycmaster.rysiek.BaseIntegrationTestTemplate;
import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import dycmaster.rysiek.sensors.sensorParsers.FlipFlopSensorParser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;


public class FileSensorIntegrationTest extends BaseIntegrationTestTemplate {

	public static final URL testFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");

    @Autowired
    FileSensor sensor;

	@Test
	public void testSensorListenerNotifies () throws Exception {
        File temp = File.createTempFile("temp-file", ".tmp");
        try {
            SensorListener listener = Mockito.mock(SensorListener.class);
            sensor.subscribeToSensor(listener);
            sensor.setFileToObserve(temp);
            sensor.startObserving();
            Files.write(temp.toPath(), "test".getBytes());
            Thread.sleep(200);
            Mockito.verify(listener, Mockito.atLeast(1)).sensorValueChangedHandler(Mockito.any(SensorValue.class));
            sensor.stopObserving();
        }finally {
            temp.delete();
        }
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
