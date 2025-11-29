package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Weather {

    @Getter @Setter
    private static String type;

    @Getter @Setter
    private static boolean desertStorm;

    @Getter @Setter
    private static String newSeason;

    @Getter @Setter
    private static int numberOfHikers;

    @Getter @Setter
    private static double windSpeed;

    @Getter @Setter
    private static double rainfall;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Weather() {
    }
}
