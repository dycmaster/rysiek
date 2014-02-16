package dycmaster.rysiek.deployment;

import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class DeploymentManagerTest {

    public static final URL TestSensorFile = FILE_DATA_PROVIDERS.class.getClassLoader().getResource("testSensor");

    @Test
    public void testDeployScriptDirs() throws Exception {
        FileDataProvider testScript = new FileDataProvider(TestSensorFile);
        Collection<FileDataProvider> scripts = new LinkedList<>();
        scripts.add(testScript);

        DeploymentManager deploymentManager = new DeploymentManager();
        deploymentManager.deployAllAndRun(scripts);
        //((FileDataProvider)(DeploymentManager.GetDeployedScripts().toArray()[0])).getProcess().waitFor();
    }
}
