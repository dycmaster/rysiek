package dycmaster.rysiek.deployment;


import dycmaster.rysiek.shared.FileTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.LinkedList;

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

    private static final Collection<FileDataProvider> _deployedScripts = CollectionUtils.synchronizedCollection(new LinkedList<FileDataProvider>());


    public static void AddDeployedScript(FileDataProvider script){
        synchronized (_deployedScripts){
            _deployedScripts.add(script);
        }
    }

    public static Collection<FileDataProvider>  GetDeployedScripts(){
        synchronized (_deployedScripts){
            return  _deployedScripts;
        }
    }




    public void deployAllAndRun(Collection<FileDataProvider> sensorScripts){


        deployScriptDirs(sensorScripts);
        deployScriptFiles(sensorScripts);

        runDeployedScripts(DeploymentManager.GetDeployedScripts());
    }


    protected void runDeployedScripts(Collection<FileDataProvider> sensorScripts){
        for(FileDataProvider script: sensorScripts){
            logger.info("Starting script: "+script.getScriptName());
            script.run();
            logger.info("Started script: "+script.getScriptName());
        }
    }



    protected void deployScriptDirs(Collection<FileDataProvider> sensorScripts) {

        logger.debug("Running dir is: " + runDir);
        Path fileSensorsDirPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir);
        logger.debug("File sensors dir is " + fileSensorsDirPath);

        prepareMainDirForSensors(fileSensorsDirPath);

        for(FileDataProvider script: sensorScripts){
            Path currScriptPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir, script.getScriptName());
            try {
                Files.createDirectory(currScriptPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected void deployScriptFiles(Collection<FileDataProvider> sensorScripts){

        for(FileDataProvider script: sensorScripts){
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

    protected void addDeployedScriptToControlTable(FileDataProvider script){
        DeploymentManager.AddDeployedScript(script);
    }

    public Collection<FileDataProvider> getAllScriptsToDeploy(){
        Collection<FileDataProvider> scripts = getScriptsFromEnum();
        scripts.addAll(getScriptsFromDir());
        return  scripts;
    }

    protected Collection<FileDataProvider> getScriptsFromEnum(){

        Collection<FileDataProvider> scripts = new LinkedList<>();
        for(FILE_DATA_PROVIDERS fileSensor:  FILE_DATA_PROVIDERS.values()){
            scripts.add( new FileDataProvider(fileSensor.getUrl()));
        }

        return scripts;
    }


    /*
     * User can also put his scripts in a specified directory
     */
    protected Collection<FileDataProvider> getScriptsFromDir(){
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
        }
    }

}
