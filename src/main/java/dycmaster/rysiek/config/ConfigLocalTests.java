package dycmaster.rysiek.config;


import dycmaster.rysiek.sensors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("localTests")
public class ConfigLocalTests {

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
}
