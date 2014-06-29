package dycmaster.rysiek.deployment;

import dycmaster.rysiek.shared.FileTools;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;


public class ScriptRunnerTest {


    public static final URL testFileDataProvider = SCRIPTS_TO_RUN.class.getClassLoader().getResource("testDataProvider");


    @Test
    public void testUrlConstructor(){
        ScriptRunner eut = new ScriptRunner(testFileDataProvider);

        Assert.assertEquals(eut.getOutputFileName(), "testFileSensor");
        Assert.assertEquals(eut.getSignalFileName(), "testFileSensorSig");
        Assert.assertTrue(eut.isEnabled());
        Assert.assertFalse(eut.isRunning());
        Assert.assertEquals(eut.getSleepTime(), 2);
        Assert.assertFalse(eut.isFailed());
    }

    @Test
    public void testReadSensorEnabled() throws Exception {
        final Boolean expexted = true;
        ScriptRunner eut = new ScriptRunner();
        File f = FileTools.openFile(testFileDataProvider);
        Boolean actual = eut.readIsSensorEnabled(f);
        Assert.assertEquals(expexted, actual);
    }

    @Test
    public void testReadOutputFileName() throws Exception {
        final String expected="testFileSensor";
        ScriptRunner eut = new ScriptRunner();
        File f = FileTools.openFile(testFileDataProvider);
        String fName = eut.readOutputFileName(f);
        Assert.assertEquals(expected, fName);
    }

    @Test
    public void testReadSignalFileName() throws Exception {
        final String expected="testFileSensorSig";
        ScriptRunner eut = new ScriptRunner();
        File f = FileTools.openFile(testFileDataProvider);
        String fName = eut.readSignalFileName(f);
        Assert.assertEquals(expected, fName);
    }

    @Test
    public void testReadSleepTime() throws Exception {
        final int expected = 2;
        ScriptRunner eut = new ScriptRunner();
        File f = FileTools.openFile(testFileDataProvider);
        int actual = eut.readSleepTime(f);
        Assert.assertEquals(expected, actual);
    }
}
