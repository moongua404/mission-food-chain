package mission;

import java.util.List;
import mission.adapter.inAdapter.InputTerminal;
import mission.adapter.outAdapter.OutputTerminal;
import mission.application.domain.DebugMode;
import mission.application.domain.dto.FishDto;
import mission.application.port.inPort.FishLoader;
import mission.application.port.inPort.Input;
import mission.application.port.outPort.Logger;

public class RuntimeConfig implements Config {
    public Input getInput() {
        return new InputTerminal();
    }

    public Logger getLogger() {
        return new OutputTerminal(DebugMode.NONE);
    }

    public FishLoader getFishLoader() {
        return new FishLoader() {
            @Override
            public List<FishDto> getFishDatabase() {
                return List.of(
                        new FishDto(1, "플랑크톤", 1, List.of()),
                        new FishDto(2, "멸치", 2, List.of(1)),
                        new FishDto(3, "정어리", 2, List.of(1)),
                        new FishDto(4, "빙어", 2, List.of(1)),
                        new FishDto(5, "고등어", 3, List.of(2, 3)),
                        new FishDto(6, "전갱이", 3, List.of(3)),
                        new FishDto(7, "꽁치", 3, List.of(2, 4)),
                        new FishDto(8, "삼치", 4, List.of(5, 6)),
                        new FishDto(9, "참치", 4, List.of(5, 6, 7)),
                        new FishDto(10, "방어", 4, List.of(6, 7)),
                        new FishDto(11, "황새치", 5, List.of(8, 10)),
                        new FishDto(12, "상어", 5, List.of(9, 10))
                );
            }
        };
    }
}
