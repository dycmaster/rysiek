package dycmaster.rysiek.triggersManager;


import com.google.common.base.Strings;
import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.shared.FileTools;
import dycmaster.rysiek.triggers2.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.quartz.collections.TimeTrigger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Manager {

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

    protected InputOnlyTrigger parseLineToInputOnlyTriggerIfPossible(String line){
        String [] values = line.split(separator);
        String tName = values[0];
        Integer inputCount = Integer.valueOf(values[2]);
        String [] inputNames = new String[inputCount];
        for(int i=0; i<inputCount; i++){
            inputNames[i] = values[3+i];
        }

        Map<Integer, String> declaredInputs = Create.newMap();
        int c=0;
        for(String str: inputNames){
            declaredInputs.put(++c, str);
        }
        String logicType = values[2+1+inputCount];
        String logicDescription = values[2+1+inputCount+1];
        TriggerLogic logic = TriggerLogic.getByParserName(logicType);
        InputOnlyTrigger trigger=null;

        switch (logic){
            case TruthTableInputLogic:
                String truthTableSingleStr = values[2+1+inputCount+1+1];

                trigger = new InputOnlyTrigger.Builder(declaredInputs, tName)
                .withLogicDescription(logicDescription)
                .withLogicType(logic.getLogicStringName())
                .withTruthTable(truthTableSingleStr, inputNames)
                .build();
                break;

            default:
                throw  new RuntimeException("Unknown logic type "+logicType);
        }

        return  trigger;
    }
    protected TimeOnlyTrigger parseLineToTimeOnlyTriggerIfPossible(String line){
        return  null;
    }
    protected TimeAndInputTrigger parseLineToTimeAndInputTriggerIfPossible(String line){

        return  null;
    }


    private static final Collection<String> COMMENT_MARKS= Create.boxIntoSet("#");
    private static final String separator=",";

    protected String getLinePartToProcess(String line){
        for (String commentMark : COMMENT_MARKS) {
            if(line.contains(commentMark)){
                String res =line.substring(0, line.indexOf(commentMark));
                return  res;
            }
        }
        return line;
    }

    protected boolean isLineToBeProcessed(String line){
        if(Strings.isNullOrEmpty(line)){
            return  false;
        }
        for(String comment: COMMENT_MARKS){
            if(line.trim().startsWith(comment)) {
                return false;
            }
        }
        return true;
    }

    protected TriggerType checkLineType(String line){
        String[] split = line.split(separator);
        if(split.length>=2){
            String trigTypeStr = split[1];
            return  TriggerType.byFriendlyName(trigTypeStr);
        }
        return  null;
    }

    private boolean isLineForTimeAndInput(String line){
        return  checkLineType(line) == TriggerType.TIME_AND_INPUT;
    }

    private boolean isLineForTimeOnlyTrigger(String line){
        return  checkLineType(line) == TriggerType.TIME;
    }

    private boolean isLineForInputOnlyTrigger(String line){
        return  checkLineType(line) == TriggerType.INPUT;
    }

    protected Collection<InputOnlyTrigger> initInputOnlyTriggersFromConfig(URL triggerDeclarations) throws IOException {
        File triggerDeclarationsFile =  FileTools.openFile(triggerDeclarations);
        return Files.lines(triggerDeclarationsFile.toPath())
                .filter(this::isLineToBeProcessed)
                .map(this::getLinePartToProcess)
                .filter(this::isLineForInputOnlyTrigger)
                .map(this::parseLineToInputOnlyTriggerIfPossible)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected Collection<TimeOnlyTrigger> initTimeOnlyTriggersFromConfig(URL triggerDeclarations) throws IOException {
        File triggerDeclarationsFile =  FileTools.openFile(triggerDeclarations);
        return Files.lines(triggerDeclarationsFile.toPath())
                .filter(this::isLineToBeProcessed)
                .map(this::getLinePartToProcess)
                .filter(this::isLineForTimeOnlyTrigger)
                .map(this::parseLineToTimeOnlyTriggerIfPossible)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected Collection<TimeAndInputTrigger> initTimeAndInputTriggersFromConfig(URL triggerDeclarations) throws IOException {
        File triggerDeclarationsFile =  FileTools.openFile(triggerDeclarations);
        return Files.lines(triggerDeclarationsFile.toPath())
                .filter(this::isLineToBeProcessed)
                .map(this::getLinePartToProcess)
                .filter(this::isLineForTimeAndInput)
                .map(this::parseLineToTimeAndInputTriggerIfPossible)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
