package dycmaster.rysiek;


import dycmaster.rysiek.config.ConfigLocalTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class,
    classes = {ConfigLocalTests.class})
@ActiveProfiles("localTests")
public class BaseContextTestTemplate {
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
}
