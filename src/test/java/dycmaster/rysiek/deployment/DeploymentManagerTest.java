package dycmaster.rysiek.deployment;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class DeploymentManagerTest {

    public static final URL TestSensorFile = FILE_SENSORS.class.getClassLoader().getResource("testSensor");

    @Test
    public void testDeployScriptDirs() throws Exception {
        FileSensorScript testScript = new FileSensorScript(TestSensorFile);
        Collection<FileSensorScript> scripts = new LinkedList<>();
        scripts.add(testScript);

        DeploymentManager deploymentManager = new DeploymentManager();
        deploymentManager.deployAllAndRun(scripts);
        //((FileSensorScript)(DeploymentManager.GetDeployedScripts().toArray()[0])).getProcess().waitFor();
    }
}
