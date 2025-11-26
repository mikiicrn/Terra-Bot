package Model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Weather {
    public String type;
    public static boolean desertStorm;
    public static String newSeason;
    public static int numberOfHikers;
    public static double windSpeed;
    public static double rainfall;
}
