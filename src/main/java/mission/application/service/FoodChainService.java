package mission.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mission.Config;
import mission.application.domain.dto.PossessingFishDto;
import mission.application.domain.exception.FishNotFound;
import mission.application.domain.dto.FishDto;
import mission.application.domain.model.FoodChain;
import mission.application.port.inPort.Input;
import mission.application.port.outPort.Logger;

public class FoodChainService {
    private final Logger logger;
    private final Input input;

    List<FishDto> fishDatabase;

    public FoodChainService(Config config) {
        this.logger = config.getLogger();
        this.input = config.getInput();
        fishDatabase = config.getFishLoader().getFishDatabase();
    }

    public void run() {
        logger.print("물고기 비로 내릴 물고기를 입력해주세요. (ex. [정어리-5],[고등어-2])");
        String response = input.getString();
        List<PossessingFishDto> fishes = parsePossessingFishDto(response);
        validateFish(fishes);
        FoodChain foodChain = new FoodChain(logger, fishDatabase, fishes);
        int survival = foodChain.calculateSurvivalPeriod();
        logger.print(String.format("%d일간 생존했습니다. ", survival));
    }

    private List<PossessingFishDto> parsePossessingFishDto(String line) {
        return Arrays.stream(line.split(","))
                .map(item -> {
                    Matcher matcher = Pattern.compile("\\[(.+?)-(\\d+)]").matcher(item);
                    matcher.find();
                    return new PossessingFishDto(matcher.group(1), Integer.parseInt(matcher.group(2)));
                })
                .toList();
    }

    private void validateFish(List<PossessingFishDto> fishes) {
        fishes.forEach(fish -> fishDatabase.stream()
                .filter(item -> Objects.equals(item.name(), fish.fishName()))
                .findAny()
                .orElseThrow(() ->
                        new FishNotFound(String.format("\"%s\" 이름의 물고기가 존재하지 않습니다.", fish.fishName())))
        );
    }
}
