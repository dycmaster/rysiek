package dycmaster.rysiek.main;


import dycmaster.rysiek.config.Config;
import dycmaster.rysiek.logicService.ILogicService;
import dycmaster.rysiek.sensors.Sensor;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) {
        //DOMConfigurator.configure(Main.class.getClassLoader().getResource("log4j.properties"));
        new Main().run();
    }


    private ILogicService logicService;


    private void run(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Config.class);
        context.refresh();
        logicService = context.getBean(ILogicService.class);


        try {
            Thread.sleep(3600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
