package dycmaster.rysiek.config;

import dycmaster.rysiek.controllers.LogicServiceController;
import dycmaster.rysiek.logicService.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("integrationTests")
public class ConfigIntegrationTests {

    @Bean
    public TriggersCreator triggersManager(){
        return  new TriggersCreator();
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
