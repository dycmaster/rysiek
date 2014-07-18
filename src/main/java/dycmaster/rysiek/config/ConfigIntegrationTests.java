package dycmaster.rysiek.config;

import dycmaster.rysiek.sensors.*;
import dycmaster.rysiek.triggersManager.Manager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("integrationTests")
public class ConfigIntegrationTests {

    @Bean
    public Manager triggersManager(){
        return  new Manager();
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
}
