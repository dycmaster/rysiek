package dycmaster.rysiek.deployment;

import java.net.URL;

public enum FILE_DATA_PROVIDERS {


    DetectUserWifi(FILE_DATA_PROVIDERS.class.getClassLoader().getResource("sensors/fileSensors/detectUserWifi")),
    CheckTemperature(FILE_DATA_PROVIDERS.class.getClassLoader().getResource("sensors/fileSensors/tempShutdown")),
    CheckSoundcard(FILE_DATA_PROVIDERS.class.getClassLoader().getResource("sensors/fileSensors/soundCardActivity"));


    private URL _url;
    private FILE_DATA_PROVIDERS(URL url){
        _url = url;
    }

    public URL getUrl(){
        return  _url;
    }

}


