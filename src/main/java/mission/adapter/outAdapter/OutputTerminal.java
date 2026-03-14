package mission.adapter.outAdapter;

import mission.application.domain.DebugMode;
import mission.application.port.outPort.Logger;

public class OutputTerminal implements Logger {
    private final DebugMode debug;

    public OutputTerminal(DebugMode debug) {
        this.debug = debug;
    }

    public OutputTerminal() {
        this.debug = DebugMode.NONE;
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void debug(String message) {
        if (debug == DebugMode.DEBUG) {
            System.out.println(message);
        }
    }

    @Override
    public void fundamentalDebug(String message) {
        if (debug == DebugMode.DEBUG || debug == DebugMode.FUNDAMENTAL_ONLY) {
            System.out.println(message);
        }
    }
}