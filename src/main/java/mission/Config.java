package mission;

import mission.application.port.inPort.FishLoader;
import mission.application.port.inPort.Input;
import mission.application.port.outPort.Logger;

public interface Config {
    public Input getInput();

    public Logger getLogger();

    public FishLoader getFishLoader();
}
