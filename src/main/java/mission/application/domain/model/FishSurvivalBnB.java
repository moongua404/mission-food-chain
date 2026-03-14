package mission.application.domain.model;

import mission.application.port.outPort.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class FishSurvivalBnB {

    private int maxSurvivalDay = 0;
    private Logger logger;

    public FishSurvivalBnB(Logger logger) {
        this.logger = logger;
    }

    public int calculateMaxSurvivalPeriod(List<Fish> initialFishes) {
        maxSurvivalDay = 0;

        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> -s.priority));
        int weight = initialFishes.stream().mapToInt(Fish::getQuantity).sum() * 2;
        queue.add(new State(deepCopy(initialFishes), 0, bound(initialFishes, 0), weight));

        while (!queue.isEmpty()) {
            State current = queue.poll();
            List<Fish> state = current.fishes;
            int day = current.day;

            logger.fundamentalDebug("=======================");
            logger.fundamentalDebug("[Day " + day + "] 상태:");
            logger.fundamentalDebug("대기열 : " + queue.size() + "개 남음");
            for (Fish f : state) {
                logger.debug(" - " + f.getName() + ": " + f.getQuantity());
            }

            if (state.stream().noneMatch(f -> f.getTrophic() > 1)) {
                maxSurvivalDay = Math.max(maxSurvivalDay, day);
                logger.fundamentalDebug("포식자 없음. 생존일: " + day);
                continue;
            }

            int predicted = current.bound;
            if (predicted < maxSurvivalDay) {
                logger.fundamentalDebug("가지치기 (bound=" + predicted + " < max=" + maxSurvivalDay + ")");
                continue;
            }

            List<List<Fish>> predatorOrders = generatePermutations(state);
            logger.fundamentalDebug("포식자 순열 " + predatorOrders.size() + "개 탐색");

            for (List<Fish> order : predatorOrders) {
                List<Fish> nextState = deepCopy(state);
                List<Fish> orderCopy = matchById(order, nextState);

                StringBuilder orderLog = new StringBuilder("순서: ");
                for (Fish f : orderCopy) orderLog.append(f.getName()).append(" → ");
                logger.debug(orderLog.toString());

                boolean changed = onDayPredation(orderCopy, nextState);
                int nextDay = day + 1;
                int nextBound = bound(nextState, nextDay);

                if (!changed) {
                    logger.fundamentalDebug("변화 없음 - 생존 중지");
                    continue;
                }

                if (nextBound < maxSurvivalDay) {
                    logger.fundamentalDebug("가지치기 (bound=" + nextBound + " < max=" + maxSurvivalDay + ")");
                    continue;
                }

                int nextWeight = nextState.stream().mapToInt(Fish::getQuantity).sum() * 2;
                queue.add(new State(nextState, nextDay, nextBound, nextWeight));
            }
        }

        return maxSurvivalDay;
    }

    private int bound(List<Fish> fishes, int currentDay) {
        Map<Integer, Integer> quantityMap = fishes.stream()
                .collect(Collectors.toMap(Fish::getId, Fish::getQuantity));

        int possibleDays = 0;
        for (Fish predator : fishes) {
            if (predator.getTrophic() <= 1 || predator.getQuantity() <= 0) continue;

            int totalPrey = predator.getPreyIds().stream()
                    .mapToInt(preyId -> quantityMap.getOrDefault(preyId, 0))
                    .sum();

            int canSurvive = Math.min(predator.getQuantity(), totalPrey);
            possibleDays = Math.max(possibleDays, canSurvive);
        }

        return currentDay + possibleDays;
    }

    private List<List<Fish>> generatePermutations(List<Fish> fishes) {
        List<Fish> predators = fishes.stream()
                .filter(f -> f.getTrophic() > 1 && f.getQuantity() > 0)
                .collect(Collectors.toList());

        List<List<Fish>> results = new ArrayList<>();
        permute(predators, 0, results);
        return results;
    }

    private void permute(List<Fish> list, int start, List<List<Fish>> result) {
        if (start == list.size()) {
            result.add(deepCopy(list));
            return;
        }
        for (int i = start; i < list.size(); i++) {
            Collections.swap(list, start, i);
            permute(list, start + 1, result);
            Collections.swap(list, start, i);
        }
    }

    private boolean onDayPredation(List<Fish> predatorOrder, List<Fish> fishes) {
        boolean changed = false;

        for (Fish predator : predatorOrder) {
            if (predator.getTrophic() == 1) continue;
            boolean survived = onSpicePredation(predator, fishes);
            if (!survived) {
                logger.debug("→ " + predator.getName() + "는 굶어 죽어 리스트에서 제거됨");
            }
            changed = true;
        }

        fishes.removeIf(f -> f.getQuantity() <= 0);

        return changed;
    }

    private boolean onSpicePredation(Fish predator, List<Fish> fishes) {
        int needed = predator.getQuantity();
        int eaten = 0;

        List<Fish> preyCandidates = fishes.stream()
                .filter(prey -> predator.getPreyIds().contains(prey.getId()))
                .sorted(Comparator.comparing(Fish::getId))
                .collect(Collectors.toList());

        for (Fish prey : preyCandidates) {
            while (prey.getQuantity() > 0 && eaten < needed) {
                prey.subQuantity();
                eaten++;
                logger.debug("→ " + predator.getName() + "가 " + prey.getName() + "를 먹음");
            }
        }

        int starved = needed - eaten;
        predator.subQuantity(starved);
        if (starved > 0) {
            logger.debug("→ " + predator.getName() + "는 먹이를 못 먹어 " + starved + "마리 굶어 죽음");
        }
        return eaten > 0;
    }

    private List<Fish> matchById(List<Fish> template, List<Fish> target) {
        Map<Integer, Fish> map = target.stream()
                .collect(Collectors.toMap(Fish::getId, f -> f));
        return template.stream()
                .map(f -> map.get(f.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Fish> deepCopy(List<Fish> original) {
        return original.stream()
                .map(Fish::copy)
                .collect(Collectors.toList());
    }

    private static class State {
        List<Fish> fishes;
        int day;
        int bound;
        int priority;

        State(List<Fish> fishes, int day, int bound, int weight) {
            this.fishes = fishes;
            this.day = day;
            this.bound = bound;
            this.priority = day * weight + bound;
        }
    }
}
