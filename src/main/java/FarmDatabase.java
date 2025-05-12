import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FarmDatabase {
    private static Lock lock = new ReentrantLock();
    private static FarmDatabase instance;
    private Map<Integer, ArrayList<String>> cattleLocations;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private FarmDatabase() {
        cattleLocations = new HashMap<>();
        scheduler.scheduleAtFixedRate(this::printAllLocationLog, 10000, 15000, TimeUnit.MILLISECONDS);
    }

    public static FarmDatabase getInstance() {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new FarmDatabase();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * @param zigbeeSignal location devices invokes this method and sends the cattle's
     *                     id and location by using Zigbee Signal.
     */
    public void updateCattleLocation(ZigbeeSignal zigbeeSignal) {
        try {
            lock.lock();
            if (!cattleLocations.containsKey(zigbeeSignal.getEarTagUniqueId())) {
                ArrayList<String> locations = new ArrayList<>();
                locations.add(zigbeeSignal.getCurrentLocation().toString());
                cattleLocations.put(zigbeeSignal.getEarTagUniqueId(), locations);
            } else {
                cattleLocations.get(zigbeeSignal.getEarTagUniqueId()).add(zigbeeSignal.getCurrentLocation().toString());
            }
        } finally {
            lock.unlock();
        }
    }

    public void printAllLocationLog() {
        System.out.println("|--------------------Cattle Location Log--------------------|");
        for (Integer cattleID : cattleLocations.keySet()){
            System.out.print("Cattle: "+cattleID+" = [");
            for(String location : cattleLocations.get(cattleID)){
                System.out.print(location+", ");
            }
            System.out.println("]");
        }
        System.out.println("|-----------------------------------------------------------|");
    }
}
