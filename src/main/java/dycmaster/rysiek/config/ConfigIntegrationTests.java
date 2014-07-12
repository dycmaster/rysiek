package dycmaster.rysiek.config;

import dycmaster.rysiek.sensors.FileSensor;
import dycmaster.rysiek.sensors.IFileObserver;
import dycmaster.rysiek.sensors.JNotifyFileObserver;
import dycmaster.rysiek.sensors.Sensor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("integrationTests")
public class ConfigIntegrationTests {

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
        return new FileSensor();
    }
}
