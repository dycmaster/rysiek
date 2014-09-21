package dycmaster.rysiek.triggers2;


import java.util.Arrays;
import java.util.Optional;

public enum TriggerLogic {

    //time only logics
    LongerThanTimeLogic("LongerThanTimeLogic", new String[]{"longerThanTime"}, TriggerLogicType.TIME_ONLY),
    SingleShotCronTimeLogic("SingleShotCron", new String[]{"cronSingleShot"}, TriggerLogicType.TIME_ONLY),

    //time and input logics
    OnLongerThanTime("OnLongerThanTime", new String[]{"onLongerThan"}, TriggerLogicType.TIME_AND_INPUT),
    OffLongerThanTime("OffLongerThanTime", new String[]{"offLongerThan"}, TriggerLogicType.TIME_AND_INPUT),

    //input only logics
    TruthTableInputLogic("TruthTableInputLogic",new String[]{"truthTable"}, TriggerLogicType.INPUT_ONLY),
    FlipFlopInputLogic("FlipFlopInputLogic", new String[]{"flipFlopInput"}, TriggerLogicType.INPUT_ONLY);


    private final String logicStringName;
    private final TriggerLogicType logicType;
    private  String[] logicParserNames;

    public static TriggerLogic getByParserName(String name){
        Optional<TriggerLogic> triggerLogicOptional = Arrays.asList(TriggerLogic.values()).stream()
                .filter(x -> Arrays.asList(x.logicParserNames).contains(name))
                .findFirst();

        return  triggerLogicOptional.isPresent()? triggerLogicOptional.get():null;
    }

    public String getLogicStringName() {
        return logicStringName;
    }

    public TriggerLogicType getLogicType() {
        return logicType;
    }


    TriggerLogic(String logicStringName, TriggerLogicType logicType){
        this(logicStringName, new String[]{}, logicType);
    }

    TriggerLogic(String logicStringName, String[] logicParserNames, TriggerLogicType logicType){
        this.logicStringName = logicStringName;
        this.logicType = logicType;
        this.logicParserNames = logicParserNames;
    }



}
