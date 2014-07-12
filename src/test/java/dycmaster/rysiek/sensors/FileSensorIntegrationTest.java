package dycmaster.rysiek.sensors;

import dycmaster.rysiek.BaseIntegrationTestTemplate;
import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.SCRIPTS_TO_RUN;
import dycmaster.rysiek.deployment.ScriptRunner;
import dycmaster.rysiek.sensors.sensorParsers.EchoSensorParser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;



@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileSensorIntegrationTest extends BaseIntegrationTestTemplate {

	public static final URL testFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");

    @Autowired
    FileSensor sensor;

    @Autowired
    SensorListener listener;

	@Test
	public void testSensorListenerNotifies () throws Exception {
        File temp = File.createTempFile("temp-file", ".tmp");
        try {
            SensorListener listener = Mockito.mock(SensorListener.class);
            sensor.addSubscriber(listener);
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

    @Test
    public void testFromFileToSensorParser () throws Exception {
        File temp = File.createTempFile("temp-file", ".tmp");
        String testStr = "test successful";
        try {
            sensor.addSubscriber(listener);
            sensor.setFileToObserve(temp);
            listener.setSensorParser(new EchoSensorParser());
            sensor.startObserving();
            listener.startListening();
            Files.write(temp.toPath(), testStr.getBytes());
            Thread.sleep(100);
            sensor.stopObserving();
            listener.stopListening();
            Map<String, String> res =  listener.getParsedParameters();
            assertEquals(res.values().stream().findFirst().get(), testStr);
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
