package dycmaster.rysiek.main;


import dycmaster.rysiek.config.Config;
import dycmaster.rysiek.deployment.DeploymentManager;
import dycmaster.rysiek.sensors.FileSensor;
import dycmaster.rysiek.sensors.Sensor;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URL;


public class Main {

    public static void main(String[] args) {
        DOMConfigurator.configure(Main.class.getClassLoader().getResource("log4j.xml"));
        new Main().run();
    }



    private void run(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Config.class);
        context.refresh();

        Sensor s = context.getBean(Sensor.class);
        s.isEnabled();
        System.out.println("fs = " + s);

    }

}
