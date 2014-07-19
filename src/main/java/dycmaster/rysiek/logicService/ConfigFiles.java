package dycmaster.rysiek.logicService;


import java.net.URL;

public enum ConfigFiles {
    TriggerDeclarations(ConfigFiles.class.getClassLoader().getResource("config/triggerDeclarations")),
    TriggersLayout(ConfigFiles.class.getClassLoader().getResource("config/triggersLayout"))
    ;



    private URL url;
    private ConfigFiles(URL url){
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
}
