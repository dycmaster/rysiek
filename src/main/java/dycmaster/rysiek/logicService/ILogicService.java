package dycmaster.rysiek.logicService;

import java.util.List;


public interface ILogicService {


    public String receiveSignal(String sensorName,
                                Boolean value,
                                String masterName,
                                String masterPass);

    public void sendActionRequest(String sourceName, boolean triggeringState, List<String> requestedAction);

    public Switchboard getSwitchboard();

    public boolean isSubscribed(String masterName, String masterPass);
    public boolean subscribe(String masterName, String masterPass, int port, String remoteHost);
}
