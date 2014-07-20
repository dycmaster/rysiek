package dycmaster.rysiek.logicService;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/processors/logicService1/{sensorName}/{value}")
public interface ILogicService {

    @POST
    public void receiveSignal(@PathParam("sensorName")String sensorName,
                              @PathParam("value")Boolean value,
                              @QueryParam("login")String masterName,
                              @QueryParam("token")String masterPass);

    public void sendActionRequest(String sourceName, boolean triggeringState, List<String> requestedAction);
    public Switchboard getSwitchboard();
}
