package entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data //generates getters, setters, toString, equals, and hashCode methods
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) // generates JSON serialization/deserialization for all fields
public abstract class Entity {
    protected String type;
    protected String name;
    protected double mass;
    protected boolean scanned = false;
    protected int scanTime = 0;
    protected int x, y;
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    protected double normalize(double score) {
        return Math.max(0, Math.min(100, score));
    }
    protected double round(double score) {
        return Math.round(score * 100.0) / 100.0;
    }
}
