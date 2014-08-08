package dycmaster.rysiek.deployment;

import java.net.URL;

public enum SCRIPTS_TO_RUN {


    DetectUserWifi("sensors/fileDataProviders/detectUserWifi"),
    CheckTemperature("sensors/fileDataProviders/tempShutdown"),
    CheckSoundcard("sensors/fileDataProviders/soundCardActivity");


    private String path;

    public String getPath() {
        return path;
    }

    private SCRIPTS_TO_RUN(String path){
        this.path = path;
    }


}


