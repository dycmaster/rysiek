package dycmaster.rysiek.sensors;


import dycmaster.rysiek.shared.Create;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileSensor extends Sensor implements FileObserverSubscriber {


	private Logger logger;
	private File fileToObserve;
	private boolean _isEnabled = false;
    private IFileObserver fObserver;

    public FileSensor(){
        logger = Logger.getLogger(this.getClass());
    }

	public FileSensor(File fileToObserve) {
        this();
		setFileToObserve(fileToObserve);
	}

    public void setFileToObserve(File fileToObserve) {
        this.fileToObserve = fileToObserve;
        this.fObserver.setObservedFile(fileToObserve);
        this.setName(String.format("%s:%s",this.getClass().getSimpleName(), fileToObserve.getName()));
    }


    @Autowired
    public void setFileObserver(IFileObserver fileObserver){
        fObserver = fileObserver;
        fObserver.subscribeToObserver(this);
    }

	@Override
	protected SensorValue getChangedEventArgs() {
		try {
            if (fileToObserve != null) {
				logger.info( String.format("%s detected change in %s", getName(), fileToObserve.getName()));
				List<String> lines = Create.newList();
                if(fileToObserve.exists()) {
                    lines = Files.readAllLines(fileToObserve.toPath(), StandardCharsets.UTF_8);
                }
                return new SensorValue(lines);
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
	public void startObserving() {
        if(fObserver != null){
            fObserver.startObserving();
            _isEnabled = true;
        }else{
            throw new RuntimeException("File observer not injected!");
        }
	}

	@Override
	public boolean isEnabled() {
		return _isEnabled;
	}

	/*
	 * This method returns immediately
	 */
	public void stopObserving() {
        fObserver.stopObserving();
        _isEnabled = false;
	}

    @Override
    public void handleFileObserverEvent(FileObserverEventArgs args) {
        this.onChangeDetected();
    }
}
