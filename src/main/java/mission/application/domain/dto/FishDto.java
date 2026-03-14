package mission.application.domain.dto;

import java.util.List;

public record FishDto(int id, String name, int trophic, List<Integer> preyIds) {
}
