package dycmaster.rysiek.controllers;

import dycmaster.rysiek.logicService.ILogicService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

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

    @RequestMapping(method=RequestMethod.GET, value="/subscribed")
    public boolean isSubscribed(@RequestParam(value = "user", required = true)String masterName,
                                @RequestParam(value = "token", required = true)String masterPass){
        return  logicService.isSubscribed(masterName, masterPass);
    }

    @RequestMapping(method=RequestMethod.GET, value="/subscribe")
    public boolean subscribe(@RequestParam(value = "user", required = true)String masterName,
                                @RequestParam(value = "token", required = true)String masterPass,
                                @RequestParam(value="port", required = true) int port,
                                WebRequest webRequest){

        String remoteHost = ((ServletWebRequest) webRequest).getRequest().getRemoteHost();
        return logicService.subscribe(masterName, masterPass, port, remoteHost);
    }



}
