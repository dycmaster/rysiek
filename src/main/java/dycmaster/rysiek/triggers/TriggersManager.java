package dycmaster.rysiek.triggers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by frs on 2/16/14.
 */
public class TriggersManager {

	private ExecutorService executor = Executors.newFixedThreadPool(5);

	private static TriggersManager _singleton;
	public static TriggersManager getInstance(){
		if(_singleton == null){
			_singleton = new TriggersManager();
		}
		return _singleton;
	}

	private TriggersManager(){
	}


	public void executeTriggerLogic(Runnable task){
		synchronized (executor){
			executor.execute(task);
		}
	}






}
