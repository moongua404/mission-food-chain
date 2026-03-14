package mission.application.port.outPort;

public interface Logger {
    void print(String message);
    void debug(String message);
    void fundamentalDebug(String message);
}
