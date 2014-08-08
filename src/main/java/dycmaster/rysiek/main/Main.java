package dycmaster.rysiek.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"dycmaster.rysiek"})
@EnableAutoConfiguration
public class Main {

    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static final String CONFIG_LOCATION = "dycmaster.rysiek.config";
    private static final String DEFAULT_PROFILE = "dev";


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }



}
