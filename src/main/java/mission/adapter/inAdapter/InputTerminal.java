package mission.adapter.inAdapter;

import api.Console;
import mission.application.port.inPort.Input;

public class InputTerminal implements Input {

    @Override
    public String getString() {
        return Console.readLine();
    }
}