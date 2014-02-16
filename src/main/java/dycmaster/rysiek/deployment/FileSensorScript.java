package dycmaster.rysiek.deployment;

import dycmaster.rysiek.shared.FileTools;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;


public class FileSensorScript {

    Logger logger = Logger.getLogger(FileSensorScript.class);

    private final String outputFileTag = "LOCAL_FILE";
    private final String isEnabledTag = "ENABLE";
    private final String signalFileTag = "LOCAL_FILE_SIGNAL";
    private final String sleepTimeTag = "SLEEP_TIME";

    private boolean isRunning;
    private boolean isEnabled;
    private String outputFileName;
    private String signalFileName;
    private int sleepTime;
    private boolean failed;
    private File sourceFile;
    private File targetFile;
    private Path runningDir;
    private Process process;

    public Process getProcess(){
        return process;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }
    public void setTargetFile(Path targetPath) {
        this.targetFile = targetPath.toFile();
        this.runningDir =  targetPath.getParent();
    }

    public File getSignalFile(){
        return  FileSystems.getDefault().getPath(targetFile.getParent(),getSignalFileName()).toFile();
    }

	public File getOutputFile(){
		return  FileSystems.getDefault().getPath(targetFile.getParent(), getOutputFileName()).toFile();
	}


    public Path getRunningDir(){
        return  runningDir;
    }

    public File getSourceFile(){
        return  sourceFile;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    private String scriptName;

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }


    public  FileSensorScript(){
        setRunning(false);
        setEnabled(false);
        setOutputFileName("");
        setSignalFileName("");
        setSleepTime(-1);
        setFailed(false);
        setScriptName("");
    }

    public  FileSensorScript(URL fileUrl){
        this(FileTools.openFile(fileUrl));
    }

    public FileSensorScript(File f){
        setSleepTime(readSleepTime(f));
        setSignalFileName(readSignalFileName(f));
        setOutputFileName(readOutputFileName(f));
        setEnabled(readSensorEnabled(f));

        setRunning(false);
        setFailed(false);
        setScriptName(f.getName());

        sourceFile = f;
    }


    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getSignalFileName() {
        return signalFileName;
    }

    public void setSignalFileName(String signalFileName) {
        this.signalFileName = signalFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }


    public void run(){

        try {
            new Thread(runner,"fileSensor runner: "+getScriptName()).start();
            Thread.sleep(1000);
            new Thread(outputMonitor,"std out monitor: "+getScriptName()).start();
            new Thread(errorMonitor,"std err monitor: "+getScriptName()).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Run the script and monitor its state
    Runnable runner = new Runnable(){

        @Override
        public void run() {
            try {
                String command = getTargetFile().toString();
                process = Runtime.getRuntime().exec("//bin//bash "+command,null, getRunningDir().toFile());
                setRunning(true);
                process.waitFor();
                setRunning(false);
                if(process.exitValue()==0){
                    setFailed(false);
                }else{
                    setFailed(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable outputMonitor = new Runnable(){
        @Override
        public void run() {
            try{
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                logger.info(getScriptName()+": " + line);
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    Runnable errorMonitor = new Runnable(){

        @Override
        public void run() {
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    logger.info("Error: "+getScriptName()+": " +line);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };






    ////////////////////////////////////////////////////////////
    // Read properties from shell scripts
    protected boolean readSensorEnabled(File scriptToDeploy){
        String l = findLineStartingWith(scriptToDeploy, isEnabledTag);
        return Boolean.parseBoolean(getValue(l));
    }

    protected String readOutputFileName(File scriptToDeploy) {
        String l = findLineStartingWith(scriptToDeploy, outputFileTag);
        return getValue(l);
    }

    protected String readSignalFileName(File scriptToDeploy){
        String l = findLineStartingWith(scriptToDeploy, signalFileTag);
        return getValue(l);
    }

    protected int readSleepTime(File scriptToDeploy){
        String l = findLineStartingWith(scriptToDeploy, sleepTimeTag);
        return Integer.parseInt(getValue(l));
    }


    ////////////////////////////////////////////////////////////
    // Methods to access variables in shell scripts

    protected String getValue(String lineWithBashVariable){
        String[] dataFLine = lineWithBashVariable.split("=");
        if (dataFLine.length >= 2) {
            String value = dataFLine[1];
            value = (value.split("\\s+"))[0];
            return value;
        }
        return "";
    }

    protected String findLineStartingWith(File file, String string) {
        try {
            boolean found = false;
            String line = "";

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    line = line.trim();
                    if (line.startsWith(string)) {
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                return line;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }



}
