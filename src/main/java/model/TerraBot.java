package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class TerraBot {
    private int x = 0;
    private int y = 0;
    private double battery;
    private int timeUntilCharged = -1;
    private List<entities.Entity> inventory = new ArrayList<>();
    private Map<String, List<String>> knowledgeBase = new LinkedHashMap<>();

    public TerraBot(final int battery) {
        this.battery = battery;
    }

    /**
     * checks if a subject (Entity) with the given name exists in the inventory
     *
     * @param name The name of the subject to find
     * @return True if found, false otherwise
     */
    public boolean findSubject(final String name) {
        return this.inventory.stream()
                .map(entities.Entity::getName)
                .anyMatch(n -> n.equals(name));
    }

    /**
     * checks if a specific fact exists within a given category in the knowledge base
     *
     * @param category The category (subject name)
     * @param fact     The specific fact to check for
     * @return True if the fact exists, false otherwise
     */
    public boolean findFact(final String category, final String fact) {
        // null check logic with 'getOrDefault'
        // if the category doesn't exist, it returns an empty list,
        // so .contains(fact) return false
        return this.knowledgeBase.getOrDefault(category, Collections.emptyList()).contains(fact);
    }

    /**
     * adds an entity to the robot's inventory
     *
     * @param entity The entity to add
     */
    public void addInventory(final entities.Entity entity) {
        this.inventory.add(entity);
    }

    /**
     * adds a fact to the robot's knowledge base
     *
     * @param category The category (subject name) to associate the fact with
     * @param fact     The fact string to add
     */
    public void addFact(final String category, final String fact) {
        this.knowledgeBase.computeIfAbsent(category, k -> new ArrayList<>()).add(fact);
    }

    /**
     * increases or decreases the battery level
     *
     * @param recharge The amount to add (can be negative for cost)
     */
    public void recharge(final int recharge) {
        this.battery += recharge;
    }

}
