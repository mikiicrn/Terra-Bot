package model;

import java.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TerraBot {
    private int x = 0;
    private int y = 0;
    private double battery;
    private int timeUntilCharged = -1;
    private List<entities.Entity> inventory = new ArrayList<>();
    private Map<String, List<String>> knowledgeBase = new LinkedHashMap<>();

    public TerraBot(int battery) {
        this.battery = battery;
    }

    public boolean findSubject(String name) {
        return this.inventory.stream().map(entities.Entity::getName).anyMatch(n -> n.equals(name));
    }

    public boolean findFact(String category, String fact) {
        // null check logic with 'getOrDefault'
        // if the category doesn't exist, it returns an empty list,
        // so .contains(fact) return false
        return this.knowledgeBase.getOrDefault(category, Collections.emptyList()).contains(fact);
    }

    public void addInventory(entities.Entity entity) {
        this.inventory.add(entity);
    }

    public void addFact(String category, String fact) {
        this.knowledgeBase.computeIfAbsent(category, k -> new ArrayList<>()).add(fact);
    }

    public void recharge(int recharge) {
        this.battery += recharge;
    }

}