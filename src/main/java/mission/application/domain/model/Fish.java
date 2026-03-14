package mission.application.domain.model;

import java.util.List;

public class Fish {
    private final int id;
    private final String name;
    private final int trophic;
    private final List<Integer> preyIds;
    private int quantity;

    public Fish(int id, String name, int trophic, List<Integer> preyIds, int quantity) {
        this.id = id;
        this.name = name;
        this.trophic = trophic;
        this.preyIds = preyIds;
        this.quantity = quantity;
    }

    public Fish copy() {
        return new Fish(id, name, trophic, preyIds, quantity);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTrophic() {
        return trophic;
    }

    public List<Integer> getPreyIds() {
        return preyIds;
    }

    public int getQuantity() {
        return quantity;
    }

    public void subQuantity() {
        this.quantity--;
    }

    public void subQuantity(int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", name, quantity);
    }
}
