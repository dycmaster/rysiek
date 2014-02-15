package dycmaster.rysiek.deployment;


import org.apache.log4j.Logger;
import java.io.File;

public class DeployAgent implements Runnable {


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    private String fname;
    static Logger logger = Logger.getLogger(DeployAgent.class);

    public DeployAgent(String filename){
        fname = filename;
    }

    @Override
    public void run() {
        try{
            File scriptToDeploy =  new File(getClass().getClassLoader().getResource("sensors/fileSensors/"+getFname()).toURI());
            logger.debug("Script to deploy is "+scriptToDeploy.getName());
            String runDir = System.getProperty("user.dir");
            logger.debug("Running dir is: "+runDir);




        }catch(Exception e){

        }

    }

}
