package dycmaster.rysiek.main;


import dycmaster.rysiek.deployment.DeploymentManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

public class Main {

    public static void main(String[] args) {

        URL url = Main.class.getClassLoader().getResource("log4j.xml");
        DOMConfigurator.configure(url);
        Logger logger = Logger.getLogger("test");
        new Main().run();
    }

    private void run(){
        DeploymentManager deploymentManager = new DeploymentManager();
        deploymentManager.deployScriptDirs();
    }

}
