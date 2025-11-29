package entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Entity {
    protected String type;
    protected String name;
    protected double mass;
    protected boolean scanned = false;
    protected int scanTime = 0;
    protected int x, y;

    private static final double MAX_SCORE = 100.0;
    private static final double ROUNDING_FACTOR = 100.0;

    /**
     * sets the position coordinates of the entity
     *
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    public final void setPosition(final int newX, final int newY) {
        this.x = newX;
        this.y = newY;
    }

    /**
     * normalizes a score to ensure it stays within valid bounds (0 to 100)
     *
     * @param score the input score to normalize
     * @return the normalized score
     */
    protected final double normalize(final double score) {
        return Math.max(0, Math.min(MAX_SCORE, score));
    }

    /**
     * rounds a double value to two decimal places
     *
     * @param score the value to round
     * @return the rounded value
     */
    protected final double round(final double score) {
        return Math.round(score * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}
