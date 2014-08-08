package dycmaster.rysiek.deployment;


import dycmaster.rysiek.shared.FileTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.LinkedList;

/*
 * This class is responsible for starting all the shell scripts (for file sensors)
 * Firstly it has to prepare a directory structure, later it has to run the scripts.
 * We run all the scripts from sensors.fileDataProviders directory
 */
public class DeploymentManager {

    Logger logger = Logger.getLogger(DeploymentManager.class);

	public DeploymentManager(){

	}

	public DeploymentManager(Collection<String> scriptsToRun){
		deployAllAndRun(parseString(scriptsToRun));
	}

	private Collection<ScriptRunner> parseString(Collection<String> scriptsCollection){
		Collection<ScriptRunner> result = new LinkedList<>();
		for(String scriptName: scriptsCollection){
			for(SCRIPTS_TO_RUN script: SCRIPTS_TO_RUN.values()){
				if(script.name().equalsIgnoreCase(scriptName)){
					result.add(new ScriptRunner(script.getPath()));
				}
			}
		}

		return  result;
	}

    /*
     * Directory to which files are deployed
     */
    private String runDir = System.getProperty("user.dir");
    private String fileSensorsDir = "fileDataProviders";

    private static final Collection<ScriptRunner> _deployedScripts = CollectionUtils.synchronizedCollection(new LinkedList<ScriptRunner>());


    public static void AddDeployedScript(ScriptRunner script){
        synchronized (_deployedScripts){
            _deployedScripts.add(script);
        }
    }

    public static Collection<ScriptRunner>  GetDeployedScripts(){
        synchronized (_deployedScripts){
            return  _deployedScripts;
        }
    }


	public void deployOneAndRun(ScriptRunner scriptRunner){
		Collection<ScriptRunner> coll = new LinkedList<>();
		coll.add(scriptRunner);
		deployAllAndRun(coll);
	}

    public void deployAllAndRun(Collection<ScriptRunner> sensorScripts){

	    Path fileSensorsDirPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir);
	    prepareMainDirForSensors(fileSensorsDirPath);
        deployScriptDirs(sensorScripts);
        deployScriptFiles(sensorScripts);

        runDeployedScripts(DeploymentManager.GetDeployedScripts());
    }


    protected void runDeployedScripts(Collection<ScriptRunner> sensorScripts){
        for(ScriptRunner script: sensorScripts){
            logger.info("Starting script: "+script.getScriptName());
            script.run();
            logger.info("Started script: "+script.getScriptName());
        }
    }



    protected void deployScriptDirs(Collection<ScriptRunner> sensorScripts) {

        logger.info("deployScriptDirs()");

        for(ScriptRunner script: sensorScripts){
            Path currScriptPath = FileSystems.getDefault().getPath(runDir, fileSensorsDir, script.getScriptName());
            try {
                Files.createDirectory(currScriptPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected void deployScriptFiles(Collection<ScriptRunner> sensorScripts){

        for(ScriptRunner script: sensorScripts){
	        logger.info("Deploying "+script.getScriptName());
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

    protected void addDeployedScriptToControlTable(ScriptRunner script){
        DeploymentManager.AddDeployedScript(script);
    }

    public Collection<ScriptRunner> getAllScriptsToDeploy(){
        Collection<ScriptRunner> scripts = getScriptsFromEnum();
        scripts.addAll(getScriptsFromDir());
        return  scripts;
    }

    protected Collection<ScriptRunner> getScriptsFromEnum(){

        Collection<ScriptRunner> scripts = new LinkedList<>();
        for(SCRIPTS_TO_RUN fileSensor:  SCRIPTS_TO_RUN.values()){
            scripts.add( new ScriptRunner(fileSensor.getPath()));
        }

        return scripts;
    }


    /*
     * User can also put his scripts in a specified directory
     */
    protected Collection<ScriptRunner> getScriptsFromDir(){
        //TODO add pulling scripts from some directory
	    return  CollectionUtils.EMPTY_COLLECTION;
    }

    protected void prepareMainDirForSensors(Path fileSensorsDirPath){
	    logger.info("Preparing main dir for sensors.");
        if (Files.exists(fileSensorsDirPath)) {
            logger.info("FileSensors directory already exists. Removing before deployment.");
            FileTools.deleteFoldersContent(fileSensorsDirPath.toFile());
            logger.info("FileSensors dir removed.");
        }

        try {
            logger.debug("Creating  "+fileSensorsDirPath.toString());
            Files.createDirectory(fileSensorsDirPath);
            logger.info(fileSensorsDirPath + " created.");
        } catch (IOException e) {
            logger.error("Unable to create FileSensors directory!!");
        }
    }

}
