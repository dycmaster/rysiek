package dycmaster.rysiek.sensors;


import dycmaster.rysiek.deployment.ScriptRunner;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileSensor extends Sensor implements FileObserverSubscriber {



	private Logger logger;
	private File fileToObserve;
	private File signalFile;
	private int fileWatchId;
	private boolean _isEnabled = false;


	public FileSensor(ScriptRunner script) {
		this(script.getOutputFile(), script.getSignalFile());
	}

	public FileSensor(File fileToObserve, File signalFile) {
		this.fileToObserve = fileToObserve;
		this.signalFile = signalFile;
		setName(fileToObserve.getName());
		logger = Logger.getLogger(FileSensor.class.getSimpleName() + ":" + getName());
	}

	@Override
	protected SensorValue getChangedEventArgs() {
		try {
			if (signalFile != null)
				logger.info(signalFile.getName() + " sensor: Change detected,");

			if (fileToObserve != null) {
				List<String> lines;
				lines = Files.readAllLines(fileToObserve.toPath(), StandardCharsets.UTF_8);

				SensorValue sv = new SensorValue(lines);
				return sv;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new SensorValue(new ArrayList<String>());
		}
	}

	/*
	 * This method returns immediately
	 */
	@Override
	public void start() {

		int mask = JNotify.FILE_MODIFIED;
		try {
			//Observation takes place in a separate thread.
			fileWatchId = JNotify.addWatch(signalFile.getPath(), mask, false, changeListener);
			logger.info(fileToObserve.getName() + " sensor: File observation started.");
			_isEnabled = true;
		} catch (JNotifyException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public boolean isEnabled() {
		return _isEnabled;
	}

	/*
	 * This method returns immediately
	 */
	public void stop() {
		boolean res = false;
		try {
			res = JNotify.removeWatch(fileWatchId);
			_isEnabled = false;
		} catch (JNotifyException e) {
			e.printStackTrace();
			logger.info(fileToObserve.getName() + " sensor: exception while removing watcher!");
		}
		if (res) {
			logger.info(fileToObserve.getName() + " sensor: watcher succesfully removed.");
		} else {
			logger.info(fileToObserve.getName() + " sensor: invalid watcher ID specified..");
		}
	}

    @Override
    public void handleFileObserverEvent(FileObserverEventArgs args) {

    }
}
