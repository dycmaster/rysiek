package dycmaster.rysiek.triggers2;


public class TriggerOutputChangedEventArgs {
    private ITrigger source;
    private boolean currState;

    public ITrigger getSource() {
        return source;
    }

    public boolean getTriggerState() {
        return currState;
    }

    public TriggerOutputChangedEventArgs(ITrigger source, boolean currState) {
        this.source = source;
        this.currState = currState;
    }
}
