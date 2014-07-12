package dycmaster.rysiek;

import dycmaster.rysiek.config.Config;
import dycmaster.rysiek.config.ConfigLocalTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class,
        classes = {Config.class})
@ActiveProfiles("production")
public class BaseIntegrationTestTemplate {
    @Test
    public void dummy(){

    }
}
