package dycmaster.rysiek.triggers2;

import dycmaster.rysiek.shared.Create;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public abstract class InputTriggerLogic extends AbstractTriggerLogic {

    public InputTriggerLogic(String description) {
        super(description);
    }

    public abstract boolean processTriggerInputs(Map<String, Boolean> triggerInputStates, Map<Integer, String> triggerDeclaredInputs);

    public static class Builder{

        private final String logicDescription;
        private TruthTable truthTable;
        private String logicType;


        public Builder withTruthTable(String[] header, List<Pair<Boolean[], Boolean>> rows){
            TruthTable currTable = new TruthTable(header);
            for (Pair<Boolean[], Boolean> row : rows) {
                currTable.addRow(row.getKey(), row.getValue());
            }
            this.truthTable = currTable;
            return  this;
        }

        public Builder withType(String logicType){
            this.logicType = logicType;
            return  this;
        }

        public Builder(String logicDescription){
            this.logicDescription = logicDescription;
        }

        public InputTriggerLogic build(){

            TriggerLogic logic = Create.newCollection(TriggerLogic.values())
                    .stream().filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch (logic){
                case TruthTableInputLogic:
                    return  new TruthTableInputLogic(logicDescription, truthTable);

                case FlipFlopInputLogic:
                    return new FlipFlopInputLogic(logicDescription);

                default:
                    throw new RuntimeException("Unknown logic type found: "+logicType);
            }
        }

    }
}
