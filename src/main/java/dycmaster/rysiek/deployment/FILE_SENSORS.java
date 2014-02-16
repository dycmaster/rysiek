package dycmaster.rysiek.deployment;

import java.net.URL;

public enum FILE_SENSORS {


    DetectUserWifi(FILE_SENSORS.class.getClassLoader().getResource("sensors/fileSensors/detectUserWifi")),
    CheckTemperature(FILE_SENSORS.class.getClassLoader().getResource("sensors/fileSensors/tempShutdown")),
    CheckSoundcard(FILE_SENSORS.class.getClassLoader().getResource("sensors/fileSensors/soundCardActivity"));


    private URL _url;
    private FILE_SENSORS(URL url){
        _url = url;
    }

    public URL getUrl(){
        return  _url;
    }

}


