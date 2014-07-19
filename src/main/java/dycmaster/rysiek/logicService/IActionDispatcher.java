package dycmaster.rysiek.logicService;


public interface IActionDispatcher {
    void dispatch(String triggerName, boolean state);
}
