import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FarmDatabase {
    private static volatile FarmDatabase instance;
    private static final Object lock = new Object();
    private Map<Integer, ArrayList<Location>> cattleLocations;

    private FarmDatabase() {
        cattleLocations = new HashMap<>();
    }

    public static FarmDatabase getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new FarmDatabase();
                }
            }
        }
        return instance;
    }
    public void updateCattleLocation(int cattleId, Location location) {
        synchronized (cattleLocations) {
        }
           // cattleLocations.put(cattleId, location);
        }
}
