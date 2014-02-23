package dycmaster.rysiek.deployment;

import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class DeploymentManagerTest {

    public static final URL TestFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");


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
