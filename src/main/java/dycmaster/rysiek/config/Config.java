package dycmaster.rysiek.config;


import dycmaster.rysiek.sensors.*;
import dycmaster.rysiek.triggersManager.Manager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("production")
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

    @Bean
    public Manager triggersManager(){
        return  new Manager();
    }




}
