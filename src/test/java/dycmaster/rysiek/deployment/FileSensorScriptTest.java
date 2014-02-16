package dycmaster.rysiek.deployment;

import dycmaster.rysiek.shared.FileTools;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Created by frs on 2/15/14.
 */
public class FileSensorScriptTest {


    public static final URL TestSensorFile = FILE_SENSORS.class.getClassLoader().getResource("testSensor");


    @Test
    public void testUrlConstructor(){
        FileSensorScript eut = new FileSensorScript(TestSensorFile);

        Assert.assertEquals(eut.getOutputFileName(), "testFileSensor");
        Assert.assertEquals(eut.getSignalFileName(), "testFileSensorSig");
        Assert.assertTrue(eut.isEnabled());
        Assert.assertFalse(eut.isRunning());
        Assert.assertEquals(eut.getSleepTime(), 20);
        Assert.assertFalse(eut.isFailed());
    }

    @Test
    public void testReadSensorEnabled() throws Exception {
        final Boolean expexted = true;
        FileSensorScript eut = new FileSensorScript();
        File f = FileTools.openFile(TestSensorFile);
        Boolean actual = eut.readSensorEnabled(f);
        Assert.assertEquals(expexted, actual);
    }

    @Test
    public void testReadOutputFileName() throws Exception {
        final String expected="testFileSensor";
        FileSensorScript eut = new FileSensorScript();
        File f = FileTools.openFile(TestSensorFile);
        String fName = eut.readOutputFileName(f);
        Assert.assertEquals(expected, fName);
    }

    @Test
    public void testReadSignalFileName() throws Exception {
        final String expected="testFileSensorSig";
        FileSensorScript eut = new FileSensorScript();
        File f = FileTools.openFile(TestSensorFile);
        String fName = eut.readSignalFileName(f);
        Assert.assertEquals(expected, fName);
    }

    @Test
    public void testReadSleepTime() throws Exception {
        final int expected = 20;
        FileSensorScript eut = new FileSensorScript();
        File f = FileTools.openFile(TestSensorFile);
        int actual = eut.readSleepTime(f);
        Assert.assertEquals(expected, actual);
    }
}
