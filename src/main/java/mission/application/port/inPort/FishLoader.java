package mission.application.port.inPort;

import java.util.List;
import mission.application.domain.dto.FishDto;

public interface FishLoader {
    List<FishDto> getFishDatabase();
}
