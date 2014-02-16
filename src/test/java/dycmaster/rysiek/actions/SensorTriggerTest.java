package dycmaster.rysiek.actions;

import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.deployment.FILE_DATA_PROVIDERS;
import dycmaster.rysiek.deployment.FileDataProvider;
import dycmaster.rysiek.sensors.FileSensor;
import dycmaster.rysiek.sensors.Sensor;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.triggers.MusicIsOnTrigger;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by frs on 2/16/14.
 */
public class SensorTriggerTest {

	private FileDataProvider deployMusicScriptAndRun(){
		FileDataProvider testScript = new FileDataProvider(FILE_DATA_PROVIDERS.CheckSoundcard.getUrl());
		Collection<FileDataProvider> scripts = new LinkedList<>();
		scripts.add(testScript);
		DeploymentManager deploymentManager = new DeploymentManager();
		deploymentManager.deployAllAndRun(scripts);
		return  testScript;
	}



	@Test
	public void testMusicTrigger () throws Exception {
		FileDataProvider testScript = deployMusicScriptAndRun();
		FileSensor fs = new FileSensor(testScript);
		fs.startObserving();

		Sensor sensor = new FileSensor(testScript);
		sensor.startObserving();

		SensorListener sensorListener = new MusicIsOnTrigger(sensor);
		sensorListener.startListening();

		Thread.sleep(30000);
	}

}
