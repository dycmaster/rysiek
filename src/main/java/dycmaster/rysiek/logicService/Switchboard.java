package dycmaster.rysiek.logicService;


import com.google.common.base.Strings;
import dycmaster.rysiek.shared.ConfigTools;
import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.shared.FileTools;
import dycmaster.rysiek.triggers2.*;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The switchboard contains triggers and routs signals between them, inputs and outputs.
 * Usage:
 * 1. set triggers
 * 2. set action dispatcher
 * 3. call init()
 */
public class Switchboard implements ITriggerListener {

    private Collection<InputOnlyTrigger> inputOnlyTriggers;
    private Collection<TimeOnlyTrigger> timeOnlyTriggers;
    private Collection<TimeAndInputTrigger> timeAndInputTriggers;
    private Map<String, Pair<String, String>> routingTable;
    private Map<String, IInputTrigger> inputTriggersMap;
    private IActionDispatcher IActionDispatcher;

    private static Logger log = Logger.getLogger(Switchboard.class);

    private static Map.Entry<String, Pair<String, String>> parseOneLineToRoutingTableRow(String line) {
        String[] parts = line.split(ConfigTools.separator);

        if (parts.length == 1) {
            return  new AbstractMap.SimpleEntry<String, Pair<String, String>>(parts[0].trim(), new Pair<>(null, null));
        } else if (parts.length == 2) {
            return  new AbstractMap.SimpleEntry<String, Pair<String, String>>(parts[0].trim(), new Pair<>(parts[1].trim(), null));
        } else {
            return  new AbstractMap.SimpleEntry<String, Pair<String, String>>(parts[0].trim(), new Pair<>(parts[1].trim(), parts[2].trim()));
        }
    }

    protected static Map<String, Pair<String, String>> initTriggersLayout(URL configFileUrl) throws IOException {
        File triggersLayoutConfig = FileTools.openFile(configFileUrl);
        return Files.lines(triggersLayoutConfig.toPath())
                .filter(ConfigTools::isLayoutConfigLineToBeProcessed)
                .map(ConfigTools::getConfigLinePartToProcess)
                .map(Switchboard::parseOneLineToRoutingTableRow)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public IActionDispatcher getActionDispatcher() {
        return IActionDispatcher;
    }

    public void setActionDispatcher(IActionDispatcher IActionDispatcher) {
        this.IActionDispatcher = IActionDispatcher;
    }

    public void dispatchSignal(String senderName, boolean value) {
        Pair<String, String> routingRowFor = findRoutingRowFor(senderName);
        if(routingRowFor==null){
            throw new RuntimeException(String.format("Routing table doesn't contain a route for a signal source=%s. " +
                    "Please verify your settings.", senderName));
        }

        //route to trigger
        if (!Strings.isNullOrEmpty(routingRowFor.getKey()) && !Strings.isNullOrEmpty(routingRowFor.getValue())) {
            String targetTrigger = routingRowFor.getKey();
            String targetInput = routingRowFor.getValue();

            if (inputTriggersMap.containsKey(targetTrigger)) {
                log.info(String.format("sending signal='%s' from %s to %s, input=%s",value, senderName, targetTrigger, targetInput));
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

    @Override
    public void triggerOutputChangedEventHandler(TriggerOutputChangedEventArgs eventArgs) {
        log.info(String.format("Handling trigger's state change. Trigger=%s, state='%s'",
                eventArgs.getSource().getName(), eventArgs.getTriggerState()));
        dispatchSignal(eventArgs.getSource().getName(), eventArgs.getTriggerState());
    }

    protected Pair<String, String> findRoutingRowFor(String name) {
        return routingTable.get(name);
    }

    public void init() {
        try {
            this.routingTable = initTriggersLayout(ConfigFiles.TriggersLayout.getUrl());
            initTriggersListening();
            initTriggerMaps();
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
                inputOnlyTrigger.addListener(this);
            }
        }

        if (timeOnlyTriggers != null) {
            for (TimeOnlyTrigger timeOnlyTrigger : timeOnlyTriggers) {
                timeOnlyTrigger.addListener(this);
            }
        }

        if (timeAndInputTriggers != null) {
            for (TimeAndInputTrigger timeAndInputTrigger : timeAndInputTriggers) {
                timeAndInputTrigger.addListener(this);
            }
        }
    }


    public Collection<InputOnlyTrigger> getInputOnlyTriggers() {
        return inputOnlyTriggers;
    }

    public void setInputOnlyTriggers(Collection<InputOnlyTrigger> inputOnlyTriggers) {
        this.inputOnlyTriggers = inputOnlyTriggers;
    }

    public Collection<TimeOnlyTrigger> getTimeOnlyTriggers() {
        return timeOnlyTriggers;
    }

    public void setTimeOnlyTriggers(Collection<TimeOnlyTrigger> timeOnlyTriggers) {
        this.timeOnlyTriggers = timeOnlyTriggers;
    }

    public Collection<TimeAndInputTrigger> getTimeAndInputTriggers() {
        return timeAndInputTriggers;
    }

    public void setTimeAndInputTriggers(Collection<TimeAndInputTrigger> timeAndInputTriggers) {
        this.timeAndInputTriggers = timeAndInputTriggers;
    }

}
