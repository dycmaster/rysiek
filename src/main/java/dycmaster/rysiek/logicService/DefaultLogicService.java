package dycmaster.rysiek.logicService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.yaml.snakeyaml.Yaml;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.nio.file.Files;
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
    public boolean isSubscribed(String masterName, String masterPass) {
        String myMaster = config.get("myMaster");
        String mastersPasswordToMe = config.get("mastersPasswordToMe");
        if(masterName.equals(myMaster) && masterPass.equals(mastersPasswordToMe)){
            if(getMyMaster() == null || !getMyMaster().getName().equals(masterName)){
                return  false;
            }else{
                return true;
            }
        }
        throw new RuntimeException("Wrong credentials!");
    }

    @Override
    public boolean subscribe(String masterName, String masterPass, int port, String remoteHost) {
        String myMaster = config.get("myMaster");
        String mastersPasswordToMe = config.get("mastersPasswordToMe");
        if(masterName.equals(myMaster) && masterPass.equals(mastersPasswordToMe)){
            setMyMaster(new MasterInfo(myMaster, port, remoteHost));
            log.info(String.format("Master %s subscribed ok and set as my master. Starting to track the Master.", getMyMaster().toString()));
            return  true;
        }

        log.info(String.format("Master %s gave bad password (%s) and was not subscribed successfully", masterName,
                masterPass));
        return false;
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
            log.info("config initiated");
            initAvahiAdvertising();
            log.info("avahi advertising initiated");
            initTrackingMaster();
            log.info("tracking master initiated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initTrackingMaster() {
        Thread masterTracker = new Thread() {
            @SuppressWarnings("InfiniteLoopStatement")
            public void run() {
                while (true) {
                    if (getMyMaster() != null) {
                        String user = config.get("loginToMaster");
                        String token = config.get("passToMaster");
                        String host = getMyMaster().getHostname();
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
            log.debug(String.format("Received signal from master=%s. SensorName=%s, value=%s",
                    masterName, sensorName, value));
            try {
                switchboard.dispatchSignal(sensorName, value);
            }catch(Exception e){
                log.error("switchboard throw an exception",e);
            }
            return "ok";
        } else {
            log.warn(String.format("Master %s sent wrong password. Pass=%s",
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
            log.info("Master not set!! Action won't be fired!");
            return;
        }

        String user = config.get("loginToMaster");
        String token = config.get("passToMaster");
        String host = getMyMaster().getHostname();
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
        String hostname;

        private MasterInfo(String name, int port, String hostname) {
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

        public String getHostname() {
            return hostname;
        }

        @Override
        public String toString() {
            return "MasterInfo{" +
                    "name='" + name + '\'' +
                    ", port=" + port +
                    ", hostname='" + hostname + '\'' +
                    '}';
        }
    }
}
