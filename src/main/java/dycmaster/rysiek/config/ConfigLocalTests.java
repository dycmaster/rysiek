package dycmaster.rysiek.config;


import dycmaster.rysiek.logicService.*;
import dycmaster.rysiek.sensors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@Configuration
@Profile("localTests")
public class ConfigLocalTests {

    @Bean
    public TriggersCreator triggersManager(){

        TriggersCreator triggersCreator = new TriggersCreator();
        try {
            triggersCreator.initTriggersFromConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return triggersCreator;
    }


    @Bean
    @Scope("prototype")
    public SensorListener sensorListener(){
        return  new DefaultSensorListener();
    }

    @Bean
    @Scope("prototype")
    public IFileObserver dummyFileObserver(){
        IFileObserver observer = new DummyFileObserver();
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
        return new FileSensor();
    }

    @Bean
    public ILogicService logicService(){
        return  new DefaultLogicService();
    }

    @Bean
    public Switchboard switchboard(){
        return  new Switchboard();
    }

    @Bean
    public IActionDispatcher actionDispatcher(){
        return  new ActionDispatcher();
    }

    @Bean
    public  IHtmlSender htmlSender(){
        return  new TestHtmlSender();
    }
}
