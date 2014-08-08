package dycmaster.rysiek.logicService;


import java.net.MalformedURLException;
import java.net.URL;

public enum ConfigFiles {
//    TriggerDeclarations(ConfigFiles.class.getClassLoader().getResource("config/triggerDeclarations")),
//    TriggersLayout(ConfigFiles.class.getClassLoader().getResource("config/triggersLayout")),
//    ActionDispatcherTable(ConfigFiles.class.getClassLoader().getResource("config/actionDispatchingTable")),
//    LogicServiceConfig(ConfigFiles.class.getClassLoader().getResource("config/logicServiceConfig"))

    TriggerDeclarations("config/triggerDeclarations"),
    TriggersLayout("config/triggersLayout"),
    ActionDispatcherTable("config/actionDispatchingTable"),
    LogicServiceConfig("config/logicServiceConfig");
    private String path;

    private ConfigFiles(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
