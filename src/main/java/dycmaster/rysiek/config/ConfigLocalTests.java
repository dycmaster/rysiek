package dycmaster.rysiek.config;


import dycmaster.rysiek.logicService.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
