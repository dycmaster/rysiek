package dycmaster.rysiek.main;


import dycmaster.rysiek.deployment.DeploymentManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

public class Main {

    public static void main(String[] args) {
        DOMConfigurator.configure(Main.class.getClassLoader().getResource("log4j.xml"));
        new Main().run();
    }

    private void run(){

        while(true){
            System.out.println("Hello world!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
