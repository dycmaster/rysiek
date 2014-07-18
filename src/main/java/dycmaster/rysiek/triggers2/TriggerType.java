package dycmaster.rysiek.triggers2;


import java.util.Arrays;
import java.util.Optional;

public enum TriggerType {
    TIME("time"),
    INPUT("input"),
    TIME_AND_INPUT(new String[]{"timeAndInput", "inputAndTime"});

    private String [] typeFriendlyNames;

    private TriggerType(String typeFriendlyName){
        this.typeFriendlyNames = new String[]{typeFriendlyName};
    }

    private TriggerType(String [] typeFriendlyNames){
        this.typeFriendlyNames = typeFriendlyNames;
    }


    public static TriggerType byFriendlyName(String typeFriendlyName){
        Optional<TriggerType> triggerTypeOptional = Arrays.asList(TriggerType.values()).stream()
                .filter(x -> Arrays.asList(x.typeFriendlyNames).contains(typeFriendlyName))
                .findFirst();
        return  triggerTypeOptional.isPresent()? triggerTypeOptional.get(): null;
    }
}
