package dycmaster.rysiek.config;


import dycmaster.rysiek.controllers.LogicServiceController;
import dycmaster.rysiek.logicService.*;
import dycmaster.rysiek.triggers2.InputOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeAndInputTrigger;
import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebMvc
public class Config {

//    TriggersCreator manager =new TriggersCreator();
//    manager.initTriggersFromConfig(ConfigFiles.TriggerDeclarations.getPath());
//    Collection<InputOnlyTrigger> inputOnlyTriggers = manager.getInputOnlyTriggers();
//    Collection<TimeAndInputTrigger> timeAndInputTriggers = manager.getTimeAndInputTriggers();
//    Collection<TimeOnlyTrigger> timeOnlyTriggers = manager.getTimeOnlyTriggers();
//
//    switchboard.setTriggersAndInit(timeOnlyTriggers, inputOnlyTriggers, timeAndInputTriggers);
    @Bean
    public TriggersCreator triggersManager(){
        TriggersCreator triggersCreator = new TriggersCreator();
        try {
            triggersCreator.initTriggersFromConfig(ConfigFiles.TriggerDeclarations.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  triggersCreator;
    }

    @Bean
    public ILogicService logicService(){
        return  new DefaultLogicService();
    }

    @Bean
    public Switchboard switchboard(){

        Switchboard switchboard = new Switchboard();

        TriggersCreator triggersCreator = triggersManager();
        Collection<InputOnlyTrigger> inputOnlyTriggers = triggersCreator.getInputOnlyTriggers();
        Collection<TimeAndInputTrigger> timeAndInputTriggers = triggersCreator.getTimeAndInputTriggers();
    Collection<TimeOnlyTrigger> timeOnlyTriggers = triggersCreator.getTimeOnlyTriggers();

       switchboard.setTriggersAndInit(timeOnlyTriggers, inputOnlyTriggers, timeAndInputTriggers);
        return switchboard;
    }

    @Bean
    public IActionDispatcher actionDispatcher(){
        return  new ActionDispatcher();
    }

    @Bean
    public  IHtmlSender htmlSender(){
        return  new DefaultHtmlSender();
    }

    @Bean
    public LogicServiceController logicServiceController(){
        return  new LogicServiceController();
    }

}
