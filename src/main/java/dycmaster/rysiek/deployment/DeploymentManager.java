package dycmaster.rysiek.deployment;


/**
 * This class is responsible for starting all the shell scripts (for file sensors)
 * Firstly it has to prepare a directory structure, later it has to run the scripts.
 * We run all the scripts from sensors.fileSensors directory
 */
public class DeploymentManager {

    public DeploymentManager(){

    }

    public void deployScriptDirs(){
        (new Thread(new DeployAgent("detectUserWifi") ) ).start();
    }

}
