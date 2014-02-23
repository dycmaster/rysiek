package dycmaster.rysiek.deployment;

import java.net.URL;

public enum SCRIPTS_TO_RUN {


    DetectUserWifi(SCRIPTS_TO_RUN.class.getClassLoader().getResource("sensors/fileDataProviders/detectUserWifi")),
    CheckTemperature(SCRIPTS_TO_RUN.class.getClassLoader().getResource("sensors/fileDataProviders/tempShutdown")),
    CheckSoundcard(SCRIPTS_TO_RUN.class.getClassLoader().getResource("sensors/fileDataProviders/soundCardActivity"));


    private URL _url;
    private SCRIPTS_TO_RUN(URL url){
        _url = url;
    }

    public URL getUrl(){
        return  _url;
    }

}


