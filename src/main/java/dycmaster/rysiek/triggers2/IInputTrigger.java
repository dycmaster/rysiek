package dycmaster.rysiek.triggers2;

/**
 * Created by frs on 7/19/14.
 */
public interface IInputTrigger {
    void setInputState(String inputName, boolean state);

    void setInputState(int inputNumber, boolean state);
}
