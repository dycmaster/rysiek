package dycmaster.rysiek.deployment;


import dycmaster.rysiek.shared.FileTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is responsible for starting all the shell scripts (for file sensors)
 * Firstly it has to prepare a directory structure, later it has to run the scripts.
 * We run all the scripts from sensors.fileSensors directory
 */
public class DeploymentManager {

    Logger logger = Logger.getLogger(DeploymentManager.class);

    /*
     * Directory to which files are deployed
     */
    private String runDir = System.getProperty("user.dir");
    private String fileSensorsDir = "fileSensors";

    private static Collection<FileSensorScript> _deployedScripts = CollectionUtils.synchronizedCollection(new LinkedList<FileSensorScript>());


    public static void AddDeployedScript(FileSensorScript script){
        synchronized (_deployedScripts){
            _deployedScripts.add(script);
        }
    }

    public static Collection<FileSensorScript>  GetDeployedScripts(){
        synchronized (_deployedScripts){
            return  _deployedScripts;
        }
    }




    public void deployAllAndRun(Collection<FileSensorScript> sensorScripts){


        deployScriptDirs(sensorScripts);
        deployScriptFiles(sensorScripts);

        runDeployedScripts(DeploymentManager.GetDeployedScripts());
    }


    protected void runDeployedScripts(Collection<FileSensorScript> sensorScripts){
        for(FileSensorScript script: sensorScripts){
            logger.info("Starting script: "+script.getScriptName());
            script.run();
            logger.info("Started script: "+script.getScriptName());
        }
    }



    protected void deployScriptDirs(Collection<FileSensorScript> sensorScripts) {

        logger.debug("Running dir is: " + runDir);
        Path fileSensorsDirPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir);
        logger.debug("File sensors dir is " + fileSensorsDirPath);

        prepareMainDirForSensors(fileSensorsDirPath);

        for(FileSensorScript script: sensorScripts){
            Path currScriptPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir, script.getScriptName());
            try {
                Files.createDirectory(currScriptPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected void deployScriptFiles(Collection<FileSensorScript> sensorScripts){

        for(FileSensorScript script: sensorScripts){
            Path sourcePath = script.getSourceFile().toPath();
            Path targetPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir, script.getScriptName(), script.getScriptName());
            script.setTargetFile(targetPath);
            try {
                Files.copy(sourcePath, targetPath);
                addDeployedScriptToControlTable(script);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void addDeployedScriptToControlTable(FileSensorScript script){
        DeploymentManager.AddDeployedScript(script);
    }

    public Collection<FileSensorScript> getAllScriptsToDeploy(){
        Collection<FileSensorScript> scripts = getScriptsFromEnum();
        scripts.addAll(getScriptsFromDir());
        return  scripts;
    }

    protected Collection<FileSensorScript> getScriptsFromEnum(){

        Collection<FileSensorScript> scripts = new LinkedList<>();
        for(FILE_SENSORS fileSensor:  FILE_SENSORS.values()){
            scripts.add( new FileSensorScript(fileSensor.getUrl()));
        }

        return scripts;
    }


    /*
     * User can also put his scripts in a specified directory
     */
    protected Collection<FileSensorScript> getScriptsFromDir(){
        //TODO add pulling scripts from some directory
        return CollectionUtils.EMPTY_COLLECTION;
    }

    protected void prepareMainDirForSensors(Path fileSensorsDirPath){
        if (Files.exists(fileSensorsDirPath)) {
            logger.info("FileSensors directory already exists. Removing before deployment.");
            FileTools.deleteFoldersContent(fileSensorsDirPath.toFile());
            logger.info("FileSensors dir removed.");
        }

        try {
            logger.debug("FileSensors directory doesn't exist. Creating..");
            Files.createDirectory(fileSensorsDirPath);
            logger.debug("FileSensors directory created.");
        } catch (IOException e) {
            logger.error("Unable to create FileSensors directory!!");
            return;
        }
    }

}
