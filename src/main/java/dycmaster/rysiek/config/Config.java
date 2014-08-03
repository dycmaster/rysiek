package dycmaster.rysiek.config;


import dycmaster.rysiek.logicService.*;
import dycmaster.rysiek.sensors.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//@ComponentScan(basePackages = {"dycmaster.rysiek.*"})
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
    public TriggersCreator triggersManager(){
        return  new TriggersCreator();
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
        return  new DefaultHtmlSender();
    }

}
