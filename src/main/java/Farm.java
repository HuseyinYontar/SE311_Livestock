// HASAN BASRİ KARSLIOĞLU
// CANERCAN DEMİR
// DEFNE YILMAZ
// HÜSEYİN YONTAR
// Project#3 Livestock

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Farm {
//       It is assumed that the boundaries are:
//       -100 <= x <= 100 and -100 <= y <= 100
//            +----------200-----------+
//            |                        |
//            |                        |
//            |          FARM          |
//           200          x            |
//            |                        |
//            |                        |
//            |                        |
//            +------------------------+

    private Farmer farmer;
    private ArrayList<Cattle> cattleList;
    private static Location farmLocation;

    public Farm() {
        cattleList = new ArrayList<>();
        farmLocation = new Location(0, 0);
    }

    /**
     * @param farmer Assigns a farmer to the farm, also sets the farmer's owned farm.
     */
    public void addFarmer(Farmer farmer) {
        this.farmer = farmer;
        farmer.setOwnedFarm(this);
    }

    /**
     * @param cattle Allows adding new cattle to the farm.
     */
    public void addCattle(Cattle cattle) {
        cattleList.add(cattle);
    }

    /**
     * @param observer Helper function for setting given observer to all cattle.
     */
    public void setObserverForAllCattle(Observer observer) {
        for (Cattle cattle : cattleList) {
            cattle.setObserver(observer);
        }
    }

    /**
     * @param visitor Helper function for allowing visitors to visit all cattle.
     */
    public void acceptVisitors(Visitor visitor) {
        for (Cattle cattle : cattleList) {
            cattle.accept(visitor);
        }
    }

    public static Location getFarmLocation() {
        return farmLocation;
    }
}

//Via visitor, farmer feeds cattle
class Farmer implements Observer, Visitor {
    private Farm ownedFarm;
    private String farmerName;
    private Set<Cattle> outCattleList = new HashSet<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Farmer(String farmerName) {
        this.farmerName = farmerName;
        scheduler.scheduleAtFixedRate(this::feedAllCattleByTimer, 7, 15, TimeUnit.SECONDS);
    }

    /**
     * @param cattle This method is used in observer - subject relationship. Cattle invokes this
     *              method if it is not in the farm boundary. The reason why this method is synchronized is that
     *               every cattle updates its location with a method that is invoked by a scheduled thread.
     */
    @Override
    public synchronized void update(Cattle cattle) {
        boolean isCattleOut = cattle.getIsOut();

        System.out.println("---------------------------");
        if (isCattleOut) {
            outCattleList.add(cattle);
            System.out.println("Cattle " + cattle.getEarTagUniqueId() + " is out now.");
        } else {
            outCattleList.remove(cattle);
            System.out.println("Cattle " + cattle.getEarTagUniqueId() + " is in now.");
        }

        String outCattleString = outCattleList.stream()
                .map(c -> String.valueOf(c.getEarTagUniqueId()))
                .collect(Collectors.joining(", "));
        System.out.println("Out cattle: " + outCattleString);
        System.out.println("---------------------------");
    }

    /**
     * @param cattle Feeds the Dairy Cattle with its corresponding diet.
     */
    @Override
    public void visit(DairyCattle cattle) {
        AbstractFoodFactory foodFactory = new DairyCattleFoodFactory();
        cattle.eat(foodFactory);
    }

    /**
     * @param cattle Feeds the Beef Cattle with its corresponding diet.
     */
    @Override
    public void visit(BeefCattle cattle) {
        AbstractFoodFactory foodFactory = new BeefCattleFoodFactory();
        cattle.eat(foodFactory);
    }

    public void setOwnedFarm(Farm farm) {
        this.ownedFarm = farm;
    }

    /**
     * Helper function for feeding all cattle in the farm. It is invoked by a timer every 15 seconds.
     */
    public void feedAllCattleByTimer() {
        System.out.println("----Feeding All Cattle-----------------------------------------------------------");
        ownedFarm.acceptVisitors(this);
        System.out.println("---------------------------------------------------------------------------------");
    }
}

interface Observer {
    void update(Cattle cattle);
}

class FarmDatabase {
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


