package mission.application.domain.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import mission.application.domain.dto.FishDto;
import mission.application.domain.dto.PossessingFishDto;
import mission.application.port.outPort.Logger;

public class FoodChain {
    private final ArrayList<Fish> fishes;
    private final FishSurvivalBnB fishSurvivalBnB;
    int day;

    public FoodChain(Logger logger, List<FishDto> database, List<PossessingFishDto> possessingFishes) {
        day = 0;
        fishes = new ArrayList<>(
                possessingFishes.stream()
                        .map(item -> {
                            FishDto fishInfo = database.stream()
                                    .filter(fish -> Objects.equals(item.fishName(), fish.name()))
                                    .findAny()
                                    .get();
                            return new Fish(fishInfo.id(), fishInfo.name(), fishInfo.trophic(),
                                    fishInfo.preyIds(), item.quantity());
                        })
                        .sorted(Comparator.comparing(Fish::getId))
                        .toList()
        );
        fishSurvivalBnB = new FishSurvivalBnB(logger);
    }

    public int calculateSurvivalPeriod() {
        return fishSurvivalBnB.calculateMaxSurvivalPeriod(fishes.stream().toList());
    }
}
