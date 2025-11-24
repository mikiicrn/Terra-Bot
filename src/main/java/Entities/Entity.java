package Entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data //generates getters, setters, toString, equals, and hashCode methods
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) // generates JSON serialization/deserialization for all fields
public abstract class Entity {
    protected String type;
    protected String name;
    protected double mass;
    protected int x, y;
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
