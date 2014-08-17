package dycmaster.rysiek.controllers;

import dycmaster.rysiek.logicService.ILogicService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processors/logicService1")
public class LogicServiceController {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(LogicServiceController.class);

    @Autowired
    private ILogicService logicService;

    @RequestMapping(method = RequestMethod.POST, value = "/{sensorName}/{value}")
    public String acceptIncomingSignal(@PathVariable String sensorName,
                                     @PathVariable Boolean value,
                                     @RequestParam(value = "user", required = true)String masterName,
                                     @RequestParam(value = "token", required = true)String masterPass){

        log.info(String.format("Got sensor value sensorName=%s, value=%s, user=%s, token=%s",
                sensorName, value, masterName, masterPass));
        return  logicService.receiveSignal(sensorName, value, masterName, masterPass);
    }

    @RequestMapping(method= RequestMethod.GET, value = "/ping")
    public String ping(){
        return "pong";
    }
}
