package dycmaster.rysiek.logicService;


import dycmaster.rysiek.controllers.LogicServiceController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader=AnnotationConfigContextLoader.class,
//        classes = {ConfigIntegrationTests.class})
//@ActiveProfiles("integrationTests")
public class LogicServiceControllerIntegrationTest {
    MockMvc mockMvc;

    @Mock
    ILogicService logicService;

    @InjectMocks
    LogicServiceController logicServiceController = new LogicServiceController();


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(logicServiceController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void thatAcceptIncomingSignal() throws Exception {
        when(logicService.receiveSignal(any(String.class), any(Boolean.class), any(String.class), any(String.class))).
                thenReturn("ok");

        this.mockMvc.perform(post("/processors/logicService1/sensor1/true?login=testLogin&token=testToken"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("\"ok\""));

    }


}
