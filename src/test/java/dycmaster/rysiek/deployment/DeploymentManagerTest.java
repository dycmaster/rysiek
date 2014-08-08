package dycmaster.rysiek.deployment;

import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class DeploymentManagerTest {

    public static final String TestFileDataProvider = "fileDataProviders/testDataProvider/testDataProvider";


	/*
	 * Deploys testDataProvider and runs it
	 * @throws Exception
	 */
    @Test
    public void testDeployScriptDirs() throws Exception {
        ScriptRunner testScript = new ScriptRunner(TestFileDataProvider);
        Collection<ScriptRunner> scripts = new LinkedList<>();
        scripts.add(testScript);

        DeploymentManager deploymentManager = new DeploymentManager();
        deploymentManager.deployAllAndRun(scripts);
    }
}
