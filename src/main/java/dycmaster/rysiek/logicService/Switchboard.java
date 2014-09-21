package dycmaster.rysiek.logicService;


import com.google.common.base.Strings;
import dycmaster.rysiek.shared.ConfigTools;
import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.shared.FileTools;
import dycmaster.rysiek.triggers2.*;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The switchboard contains triggers and routs signals between them, inputs and outputs.
 * Usage:
 * 1. set action dispatcher (mandatory)
 * 2. call setTriggersAndInit (optional)
 */
public class Switchboard implements ITriggerListener {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(Switchboard.class);
    private Collection<InputOnlyTrigger> inputOnlyTriggers;
    private Collection<TimeOnlyTrigger> timeOnlyTriggers;
    private Collection<TimeAndInputTrigger> timeAndInputTriggers;
    private Map<String, List<Pair<String, String>>> routingTable;
    private Map<String, IInputTrigger> inputTriggersMap = Create.newMap();
    private IActionDispatcher IActionDispatcher;

    public Switchboard(){
        initRoutingTable();
    }

    private static Map.Entry<String, List<Pair<String, String>>> parseOneLineToRoutingTableRow(String line) {
        String[] parts = line.split(ConfigTools.separator);

        if (parts.length == 1) {
            return new AbstractMap.SimpleEntry<String, List<Pair<String, String>>>(parts[0].trim(), Create.newList(new Pair<>(null, null)));
        } else if (parts.length == 2) {
            return new AbstractMap.SimpleEntry<String, List<Pair<String, String>>>(parts[0].trim(), Create.newList(new Pair<String,String>(parts[1].trim(), null)));
        } else {
            return new AbstractMap.SimpleEntry<String, List<Pair<String, String>>>(parts[0].trim(), Create.newList(new Pair<>(parts[1].trim(), parts[2].trim())));
        }
    }

    protected static Map<String, List<Pair<String, String>>> initTriggersLayout(String configFilePath) throws IOException {
        File triggersLayoutConfig = FileTools.openFile(configFilePath);
        return Files.lines(triggersLayoutConfig.toPath())
                .filter(ConfigTools::isLayoutConfigLineToBeProcessed)
                .map(ConfigTools::getConfigLinePartToProcess)
                .map(Switchboard::parseOneLineToRoutingTableRow)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> {
                    x.addAll(y);
                    return x;
                }));
    }

    public IActionDispatcher getActionDispatcher() {
        return IActionDispatcher;
    }

    @Required
    @Autowired
    public void setActionDispatcher(IActionDispatcher IActionDispatcher) {
        this.IActionDispatcher = IActionDispatcher;
    }

    public void dispatchSignal(String senderName, boolean value) {
        List<Pair<String, String>> routingsRowFor = findRoutingRowFor(senderName);


        for (Pair<String, String> routingRowFor : routingsRowFor) {
            if (routingRowFor == null) {
                throw new RuntimeException(String.format("Routing table doesn't contain a route for a signal source=%s. " +
                        "Please verify your settings.", senderName));
            }

            //route to trigger
            if (!Strings.isNullOrEmpty(routingRowFor.getKey()) && !Strings.isNullOrEmpty(routingRowFor.getValue())) {
                String targetTrigger = routingRowFor.getKey();
                String targetInput = routingRowFor.getValue();

                if (inputTriggersMap.containsKey(targetTrigger)) {
                    log.info(String.format("sending signal='%s' from %s to %s, input=%s", value, senderName, targetTrigger, targetInput));
                    inputTriggersMap.get(targetTrigger).setInputState(targetInput, value);
                } else {
                    throw new RuntimeException(String.format("Source %s requested to send its output signal to trigger %s to input %s." +
                                    " Target trigger %s is not present in the system. Please verify your settings.",
                            senderName, targetTrigger, targetInput, targetTrigger));
                }

            } else {  //route to action dispatcher
                log.info(String.format("sending signal='%s' from %s to actionDispatcher", value, senderName));
                IActionDispatcher.dispatch(senderName, value);
            }
        }
    }

    @Override
    public void triggerOutputChangedEventHandler(TriggerOutputChangedEventArgs eventArgs) {
        log.info(String.format("Handling trigger's state change. Trigger=%s, state='%s'",
                eventArgs.getSource().getName(), eventArgs.getTriggerState()));
        dispatchSignal(eventArgs.getSource().getName(), eventArgs.getTriggerState());
    }

    protected List<Pair<String, String>> findRoutingRowFor(String name) {
        return routingTable.get(name);
    }

    public void setTriggersAndInit(Collection<TimeOnlyTrigger> timeTriggers,
                                   Collection<InputOnlyTrigger> inputOnlyTriggers,
                                   Collection<TimeAndInputTrigger> timeAndInputTriggers){
        setInputOnlyTriggers(inputOnlyTriggers);
        setTimeAndInputTriggers(timeAndInputTriggers);
        setTimeOnlyTriggers(timeTriggers);

        initTriggersListening();
        initTriggerMaps();

    }

    protected void initRoutingTable() {
        try {
            this.routingTable = initTriggersLayout(ConfigFiles.TriggersLayout.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTriggerMaps() {
        inputTriggersMap = Create.newMap();
        if (inputOnlyTriggers != null) {
            for (InputOnlyTrigger inputOnlyTrigger : inputOnlyTriggers) {
                inputTriggersMap.put(inputOnlyTrigger.getName(), inputOnlyTrigger);
            }
        }
        if (timeAndInputTriggers != null) {
            for (TimeAndInputTrigger timeAndInputTrigger : timeAndInputTriggers) {
                inputTriggersMap.put(timeAndInputTrigger.getName(), timeAndInputTrigger);
            }
        }
    }

    private void initTriggersListening() {

        if (inputOnlyTriggers != null) {
            for (InputOnlyTrigger inputOnlyTrigger : inputOnlyTriggers) {
                inputOnlyTrigger.removeListener(this);
                inputOnlyTrigger.addListener(this);
            }
        }

        if (timeOnlyTriggers != null) {
            for (TimeOnlyTrigger timeOnlyTrigger : timeOnlyTriggers) {
                timeOnlyTrigger.removeListener(this);
                timeOnlyTrigger.addListener(this);
            }
        }

        if (timeAndInputTriggers != null) {
            for (TimeAndInputTrigger timeAndInputTrigger : timeAndInputTriggers) {
                timeAndInputTrigger.removeListener(this);
                timeAndInputTrigger.addListener(this);
            }
        }
    }


    public Collection<InputOnlyTrigger> getInputOnlyTriggers() {
        return inputOnlyTriggers;
    }

    protected void setInputOnlyTriggers(Collection<InputOnlyTrigger> inputOnlyTriggers) {
        this.inputOnlyTriggers = inputOnlyTriggers;
    }

    public Collection<TimeOnlyTrigger> getTimeOnlyTriggers() {
        return timeOnlyTriggers;
    }

    protected void setTimeOnlyTriggers(Collection<TimeOnlyTrigger> timeOnlyTriggers) {
        this.timeOnlyTriggers = timeOnlyTriggers;
    }

    public Collection<TimeAndInputTrigger> getTimeAndInputTriggers() {
        return timeAndInputTriggers;
    }

    protected void setTimeAndInputTriggers(Collection<TimeAndInputTrigger> timeAndInputTriggers) {
        this.timeAndInputTriggers = timeAndInputTriggers;
    }

}
