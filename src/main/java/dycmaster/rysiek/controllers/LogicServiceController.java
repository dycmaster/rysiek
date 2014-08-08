package dycmaster.rysiek.controllers;

import dycmaster.rysiek.logicService.ILogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processors/logicService1")
public class LogicServiceController {


    private ILogicService logicService;

    @Autowired
    public void setLogicService(ILogicService logicService) {
        this.logicService = logicService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{sensorName}/{value}")
    public String acceptIncomingSignal(@PathVariable String sensorName,
                                     @PathVariable Boolean value,
                                     @RequestParam(value = "login", required = true)String masterName,
                                     @RequestParam(value = "token", required = true)String masterPass){

        return  logicService.receiveSignal(sensorName, value,masterName, masterPass);
    }
}
