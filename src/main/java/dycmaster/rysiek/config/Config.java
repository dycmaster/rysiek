package dycmaster.rysiek.config;


import dycmaster.rysiek.controllers.LogicServiceController;
import dycmaster.rysiek.logicService.*;
import dycmaster.rysiek.sensors.*;
import dycmaster.rysiek.triggers2.InputOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeAndInputTrigger;
import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import org.springframework.context.annotation.*;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebMvc
public class Config {

    @Bean
    @Scope("prototype")
    public SensorListener sensorListener(){
        return  new DefaultSensorListener();
    }

    @Bean
    @Scope("prototype")
    public IFileObserver jnotifyFileObserver(){
        IFileObserver observer = new JNotifyFileObserver();
        return  observer;
    }

    @Bean
    @Scope("prototype")
    public Sensor sensor(){
        Sensor s = new FileSensor();
        return  s;
    }

    @Bean
    @Scope("prototype")
    public FileSensor fileSensor(){
        FileSensor s = new FileSensor();
        return  s;
    }

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
