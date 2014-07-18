package dycmaster.rysiek.triggersManager;

import dycmaster.rysiek.BaseContextTestTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.*;

public class ManagerTest extends BaseContextTestTemplate {

    @Autowired
    private Manager manager;

    @Test
    public void testInitTriggersFromConfig() throws IOException {
        manager.initTriggersFromConfig(ConfigFiles.TriggerDeclarations.getUrl());
    }

}