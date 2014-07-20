package dycmaster.rysiek.logicService;


import dycmaster.rysiek.config.Config;

import java.net.URL;

public enum ConfigFiles {
    TriggerDeclarations(ConfigFiles.class.getClassLoader().getResource("config/triggerDeclarations")),
    TriggersLayout(ConfigFiles.class.getClassLoader().getResource("config/triggersLayout")),
    ActionDispatcherTable(ConfigFiles.class.getClassLoader().getResource("config/actionDispatchingTable")),
    LogicServiceConfig(ConfigFiles.class.getClassLoader().getResource("config/logicServiceConfig"))
    ;



    private URL url;
    private ConfigFiles(URL url){
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
}
