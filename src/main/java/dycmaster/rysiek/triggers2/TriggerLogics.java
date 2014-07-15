package dycmaster.rysiek.triggers2;


public enum TriggerLogics {

    //time only logics
    LongerThanTimeLogic("LongerThanTimeLogic", TriggerLogicType.TIME_ONLY),
    SingleShotCronTimeLogic("SingleShotCron", TriggerLogicType.TIME_ONLY),

    //time and input logics
    OnLongerThanTime("OnLongerThanTime", TriggerLogicType.TIME_AND_INPUT),

    //input only logics
    TruthTableInputLogic("TruthTableInputLogic", TriggerLogicType.INPUT_ONLY),
    FlipFlopInputLogic("FlipFlopInputLogic", TriggerLogicType.INPUT_ONLY);


    private final String logicStringName;
    private final TriggerLogicType logicType;

    public String getLogicStringName() {
        return logicStringName;
    }

    public TriggerLogicType getLogicType() {
        return logicType;
    }



    TriggerLogics(String logicStringName, TriggerLogicType logicType){

        this.logicStringName = logicStringName;
        this.logicType = logicType;
    }



}
