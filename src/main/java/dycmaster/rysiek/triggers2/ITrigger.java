package dycmaster.rysiek.triggers2;


import javafx.util.Pair;
import org.joda.time.DateTime;

import java.util.List;


public interface ITrigger {

    boolean getOutputState();
    DateTime getLastChangeTime();
    List<Pair<DateTime, Boolean>> getTriggerHistory();
    ITriggerLogic getTriggerLogic();

    void startTriggering();
    void stopTriggering();
    boolean isEnabled();
    String getName();
    void setDescription(String description);
    String getDescription();

    void addListener(ITriggerListener subscriber);
    void removeListener(ITriggerListener subscriber);
}
