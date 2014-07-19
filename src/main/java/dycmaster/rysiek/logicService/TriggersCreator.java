package dycmaster.rysiek.logicService;


import dycmaster.rysiek.shared.ConfigTools;
import dycmaster.rysiek.shared.FileTools;
import dycmaster.rysiek.triggers2.InputOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeAndInputTrigger;
import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import dycmaster.rysiek.triggers2.TriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class TriggersCreator {

    Logger _log = LoggerFactory.getLogger(this.getClass());
    private Collection<InputOnlyTrigger> inputOnlyTriggers;
    private Collection<TimeAndInputTrigger> timeAndInputTriggers;
    private Collection<TimeOnlyTrigger> timeOnlyTriggers;

    public Collection<InputOnlyTrigger> getInputOnlyTriggers() {
        return inputOnlyTriggers;
    }

    public Collection<TimeAndInputTrigger> getTimeAndInputTriggers() {
        return timeAndInputTriggers;
    }

    public Collection<TimeOnlyTrigger> getTimeOnlyTriggers() {
        return timeOnlyTriggers;
    }

    public void initTriggersFromConfig(URL triggerDeclarations) throws IOException {
        inputOnlyTriggers = initInputOnlyTriggersFromConfig(triggerDeclarations);
        timeOnlyTriggers = initTimeOnlyTriggersFromConfig(triggerDeclarations);
        timeAndInputTriggers = initTimeAndInputTriggersFromConfig(triggerDeclarations);
    }

    protected InputOnlyTrigger parseLineToInputOnlyTriggerIfPossible(String line) {
        return new InputOnlyTrigger.Builder().fromSingleString(line, ConfigTools.separator).build();
    }

    protected TimeOnlyTrigger parseLineToTimeOnlyTriggerIfPossible(String line) {
        return new TimeOnlyTrigger.Builder().fromSingleString(line, ConfigTools.separator).build();
    }

    protected TimeAndInputTrigger parseLineToTimeAndInputTriggerIfPossible(String line) {
        return new TimeAndInputTrigger.Builder().fromSingleString(line, ConfigTools.separator).build();
    }

    protected TriggerType checkLineType(String line) {
        String[] split = line.split(ConfigTools.separator);
        if (split.length >= 2) {
            String trigTypeStr = split[1];
            return TriggerType.byFriendlyName(trigTypeStr);
        }
        return null;
    }

    private boolean isLineForTimeAndInput(String line) {
        return checkLineType(line) == TriggerType.TIME_AND_INPUT;
    }

    private boolean isLineForTimeOnlyTrigger(String line) {
        return checkLineType(line) == TriggerType.TIME;
    }

    private boolean isLineForInputOnlyTrigger(String line) {
        return checkLineType(line) == TriggerType.INPUT;
    }

    protected Collection<InputOnlyTrigger> initInputOnlyTriggersFromConfig(URL triggerDeclarations) throws IOException {
        File triggerDeclarationsFile = FileTools.openFile(triggerDeclarations);
        return Files.lines(triggerDeclarationsFile.toPath())
                .filter(ConfigTools::isConfigLineToBeProcessed)
                .map(ConfigTools::getConfigLinePartToProcess)
                .filter(this::isLineForInputOnlyTrigger)
                .map(this::parseLineToInputOnlyTriggerIfPossible)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected Collection<TimeOnlyTrigger> initTimeOnlyTriggersFromConfig(URL triggerDeclarations) throws IOException {
        File triggerDeclarationsFile = FileTools.openFile(triggerDeclarations);
        return Files.lines(triggerDeclarationsFile.toPath())
                .filter(ConfigTools::isConfigLineToBeProcessed)
                .map(ConfigTools::getConfigLinePartToProcess)
                .filter(this::isLineForTimeOnlyTrigger)
                .map(this::parseLineToTimeOnlyTriggerIfPossible)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected Collection<TimeAndInputTrigger> initTimeAndInputTriggersFromConfig(URL triggerDeclarations) throws IOException {
        File triggerDeclarationsFile = FileTools.openFile(triggerDeclarations);
        return Files.lines(triggerDeclarationsFile.toPath())
                .filter(ConfigTools::isConfigLineToBeProcessed)
                .map(ConfigTools::getConfigLinePartToProcess)
                .filter(this::isLineForTimeAndInput)
                .map(this::parseLineToTimeAndInputTriggerIfPossible)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
