package dycmaster.rysiek.actions;


import dycmaster.rysiek.deployment.FileSensorScript;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;


public class FileSensor extends Sensor {

	private static Logger logger = Logger.getLogger(FileSensor.class);

	JNotifyListener changeListener = new JNotifyListener() {
		@Override
		public void fileCreated(int i, String s, String s2) {
		}

		@Override
		public void fileDeleted(int i, String s, String s2) {
		}

		@Override
		public void fileModified(int wd, String rootPath, String name) {

			try {
				logger.info(signalFile.getName() + " sensor: Change detected!!");

				List<String> lines = new LinkedList<>();
				try {
					lines = Files.readAllLines(fileToObserve.toPath(), StandardCharsets.UTF_8);
				} catch (IOException e) {
					e.printStackTrace();
				}

				SensorValue sv = new SensorValue(lines);
				observedEntityChanged(sv);


			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void fileRenamed(int i, String s, String s2, String s3) {

		}
	};
	private File fileToObserve;
	private File signalFile;
	private int fileWatchId;

	public FileSensor(FileSensorScript script) {
		this.fileToObserve = script.getOutputFile();
		this.signalFile = script.getSignalFile();
	}


	public FileSensor(File fileToObserve, File signalFile) {
		this.fileToObserve = fileToObserve;
		this.signalFile = signalFile;
	}

	/*
	 * This method returns immediately
	 */
	@Override
	public void startObserving() {

		int mask = JNotify.FILE_MODIFIED;
		try {
			//Observation takes place in a separate thread.
			fileWatchId = JNotify.addWatch(signalFile.getPath(), mask, false, changeListener);
			logger.info(fileToObserve.getName() + " sensor: File observation started.");
		} catch (JNotifyException e) {
			logger.error(e.getMessage());
		}
	}

	/*
	 * This method returns immediately
	 */
	public void stopObserving() {
		boolean res = false;
		try {
			res = JNotify.removeWatch(fileWatchId);
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


}
