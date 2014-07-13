package dycmaster.rysiek.triggers2;


public class AbstractTriggerLogic implements ITriggerLogic {

    private final String description;

    public AbstractTriggerLogic(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
