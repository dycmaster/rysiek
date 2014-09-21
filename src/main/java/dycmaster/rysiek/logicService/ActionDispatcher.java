package dycmaster.rysiek.logicService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActionDispatcher extends AbstractActionDispatcher {
    private static Logger log = LoggerFactory.getLogger(ActionDispatcher.class);
    private BlockingQueue<DispatcherCommand> _tasks = new LinkedBlockingQueue<>();

    public ActionDispatcher(){
        CommandsHandler commandsHandler = new CommandsHandler(_tasks);
        new Thread(commandsHandler).start();
    }

    private class CommandsHandler implements Runnable{
        private BlockingQueue<DispatcherCommand> _queue;

        private CommandsHandler(BlockingQueue<DispatcherCommand> queue) {
            this._queue = queue;
        }

        @Override
        public void run() {
            while(true){
                try {
                    DispatcherCommand dc = _queue.take();
                    handleDispatcherCommand(dc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleDispatcherCommand(DispatcherCommand ds){
        String triggerName = ds.getTriggerName();
        boolean state = ds.getState();
        List<String> actionsToFire = findActionsToFire(triggerName, state);
        log.info(String.format("Action dispatch request received. Trigger name=%s, state=%s. Actions to be fired: %s",
                triggerName, state, actionsToFire.toString()));
        getLogicService().sendActionRequest(triggerName, state, actionsToFire);
    }

    @Override
    public void dispatch(String triggerName, boolean state) {
        DispatcherCommand ds = new DispatcherCommand(triggerName, state);
        try {
            _tasks.put(ds);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private class DispatcherCommand{
        private String triggerName;
        private boolean state;

        private DispatcherCommand(String triggerName, boolean state) {
            this.triggerName = triggerName;
            this.state = state;
        }

        public String getTriggerName() {
            return triggerName;
        }

        public boolean getState() {
            return state;
        }
    }
}
