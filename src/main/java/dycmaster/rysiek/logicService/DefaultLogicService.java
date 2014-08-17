package dycmaster.rysiek.logicService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.yaml.snakeyaml.Yaml;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultLogicService  implements ILogicService  {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(DefaultLogicService.class);
    private final Object lock = new Object();
    ServiceInfo myServiceInfo;
    private Map<String, String> config;
    private JmDNS jmDNS;
    private MasterInfo myMaster;
    private Switchboard switchboard;
    private IHtmlSender htmlSender;

    public DefaultLogicService(){
        initService();
    }

    @Override
    public Switchboard getSwitchboard() {
        return switchboard;
    }

    @Autowired
    @Required
    public void setHtmlSender(IHtmlSender htmlSender){
        this.htmlSender = htmlSender;
    }

    @Autowired
    @Required
    public void setSwitchboard(Switchboard switchboard) {
        this.switchboard = switchboard;
    }

    protected MasterInfo getMyMaster() {
        synchronized (lock) {
            return myMaster;
        }
    }

    protected void setMyMaster(MasterInfo myMaster) {
        synchronized (lock) {
            this.myMaster = myMaster;
        }
    }

    protected void initService() {
        try {
            jmDNS = JmDNS.create();
            initConfig();
            initAvahiAdvertising();
            initAvahiListening(config.get("masterService"));
            initTrackingMaster();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAvahiListening(String masterService) throws IOException {
        jmDNS.addServiceListener(masterService, new ServiceListener() {
            @Override
            public void serviceAdded(ServiceEvent serviceEvent) {
                ServiceInfo info = serviceEvent.getDNS().getServiceInfo(serviceEvent.getType(), serviceEvent.getName());
                int port = info.getPort();
                String[] hostAddresses = info.getHostAddresses();
                String name = info.getName();
                String myMaster = config.get("myMaster");
                if (!name.equals(myMaster)) {
                    log.info("This is not my master.. found master=" + name);
                    return;
                }
                setMyMaster(new MasterInfo(name, port, hostAddresses));
                log.info(String.format("New master found and set. name=%s, host=%s, port=%d",
                        name, hostAddresses[0], port));
            }

            @Override
            public void serviceRemoved(ServiceEvent serviceEvent) {
            }

            @Override
            public void serviceResolved(ServiceEvent serviceEvent) {

            }
        });
    }

    private void initTrackingMaster() {
        Thread masterTracker = new Thread() {
            @SuppressWarnings("InfiniteLoopStatement")
            public void run() {
                while (true) {
                    if (getMyMaster() != null) {
                        String user = config.get("loginToMaster");
                        String token = config.get("passToMaster");
                        String host = getMyMaster().getHostname()[0];
                        int port = getMyMaster().getPort();
                        String mName = getMyMaster().getName();
                        String url = String.format("http://%s:%s/masters/%s/ping?user=%s&token=%s",
                                host, port, mName, user, token);
                        int respCode = 0;
                        try {
                            respCode = htmlSender.senGet(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(!(respCode>=200 && respCode<300)){
                            log.info(String.format("Response code received: %s. Removing current master.",respCode));
                            setMyMaster(null);
                        }
                    }
                    try {
                        Thread.sleep(Long.parseLong(config.get("masterTrackerSleep")));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        masterTracker.start();
    }

    private void initAvahiAdvertising() throws IOException {
        String myName = config.get("myName");
        String myType = config.get("logicService");
        int myPort = Integer.parseInt(config.get("myPort"));
        String myDesc = config.get("myDesc");
        String service_key = "description"; // Max 9 chars
        HashMap<String, byte[]> properties = new HashMap<String, byte[]>();
        properties.put(service_key, myDesc.getBytes());
        myServiceInfo = ServiceInfo.create(myType, myName,
                myPort, 0,0,true, properties );
        jmDNS.registerService(myServiceInfo);
    }

    private void initConfig() {
        Yaml yamlCfg = new Yaml();
        try {
            config = (Map<String, String>) yamlCfg.load(Files.newInputStream(Paths.get(ConfigFiles.LogicServiceConfig.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String receiveSignal(String sensorName, Boolean value, String masterName,String masterPass) {
        if (masterName.equals(config.get("myMaster")) && masterPass.equals(config.get("mastersPasswordToMe"))) {
            log.info(String.format("Received signal from master=%s. SensorName=%s, value=%s",
                    masterName, sensorName, value));
            try {
                switchboard.dispatchSignal(sensorName, value);
            }catch(Exception e){
                log.info("switchboard throw an exception",e);
            }
            return "ok";
        } else {
            log.info(String.format("Master %s sent wrong password. Pass=%s",
                    masterName, masterPass));
            throw  new RuntimeException("forbidden");
        }
    }


    /**
     * This has to send action request to a known master
     *
     * @param sourceName
     * @param triggeringState
     * @param requestedAction
     */
    @Override
    public void sendActionRequest(String sourceName, boolean triggeringState, List<String> requestedAction) {
        if(requestedAction.size()==0){
            log.info("No actions to be fired.");
            return;
        }
        if(getMyMaster()==null){
            log.info("Master not set yet.");
            return;
        }

        String user = config.get("loginToMaster");
        String token = config.get("passToMaster");
        String host = getMyMaster().getHostname()[0];
        int port = getMyMaster().getPort();
        String mName = getMyMaster().getName();

        for (String action : requestedAction) {
            String url = String.format("http://%s:%s/masters/%s/action/%s?user=%s&token=%s",
                    host, port, mName, action, user, token);
            try {
                int respCode = htmlSender.senGet(url);
                log.info("Request sent. Response code is:" + respCode);
            } catch (Exception e) {
                log.error(String.format("Exception occurred while sending GET to master %s. URL was: %s",
                        mName, url), e);
            }
        }
    }

    private class MasterInfo {
        String name;
        int port;
        String[] hostname;

        private MasterInfo(String name, int port, String[] hostname) {
            this.name = name;
            this.port = port;
            this.hostname = hostname;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }

        public String[] getHostname() {
            return hostname;
        }
    }
}
