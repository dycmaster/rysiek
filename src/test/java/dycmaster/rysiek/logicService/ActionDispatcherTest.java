package dycmaster.rysiek.logicService;

import dycmaster.rysiek.BaseContextTestTemplate;
import javafx.util.Pair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ActionDispatcherTest extends BaseContextTestTemplate {

    @Autowired
    public IActionDispatcher actionDispatcher;

    @Test
    public void testReadDispatchingTableFromFile() throws Exception {
        Map<String, List<Pair<Boolean, String>>> tableFromFile = AbstractActionDispatcher.readDispatchingTableFromFile(ConfigFiles.ActionDispatcherTable.getPath());
        assertNotNull(tableFromFile);
    }

    @Test
    public void testDispatchAction(){
        actionDispatcher.dispatch("trigger2", true);

    }
}