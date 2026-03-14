package mission;

import api.Console;
import mission.application.service.FoodChainService;

public class Application {
    public static void main(String[] args) {
        //TODO: 미션 구현
        Config config = new RuntimeConfig();
        FoodChainService foodChainService = new FoodChainService(config);
        foodChainService.run();
    }
}
