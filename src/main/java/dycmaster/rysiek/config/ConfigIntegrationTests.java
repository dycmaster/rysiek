package dycmaster.rysiek.config;

import dycmaster.rysiek.controllers.LogicServiceController;
import dycmaster.rysiek.logicService.*;
import dycmaster.rysiek.sensors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("integrationTests")
public class ConfigIntegrationTests {

    @Bean
    public TriggersCreator triggersManager(){
        return  new TriggersCreator();
    }

    @Bean
    @Scope("prototype")
    public SensorListener sensorListener(){
        return  new DefaultSensorListener();
    }

    @Bean
    @Scope("prototype")
    public IFileObserver jnotifyFileObserver(){
        return new JNotifyFileObserver();
    }

    @Bean
    @Scope("prototype")
    public Sensor sensor(){
        return new FileSensor();
    }

    @Bean
    @Scope("prototype")
    public FileSensor fileSensor(){
        return new FileSensor();
    }


    ///////////

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
        return  new DefaultHtmlSender();
    }

    @Bean
    public LogicServiceController logicServiceController(){
        return  new LogicServiceController();
    }
}
