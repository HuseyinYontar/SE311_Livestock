import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Farm{
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
    private Location farmLocation;
    private static final int horizontal_edge_length = 200;
    private static final int vertical_edge_length = 200;

    public Farm(){
        cattleList = new ArrayList<>();
        farmLocation = new Location(0,0);
    }

    public void addFarmer(Farmer farmer){
        this.farmer = farmer;
        farmer.setOwnedFarm(this);
    }

    public void addCattle(Cattle cattle){
        cattleList.add(cattle);
    }

    public void setAllCattleObservers(Observer observer){
        for (Cattle cattle : cattleList){
            cattle.setObserver(observer);
        }
    }

    public void acceptVisitors(Visitor visitor){
        for (Cattle cattle : cattleList){
            cattle.accept(visitor);
        }
    }

    public static int get_horizontal_edge_length(){return horizontal_edge_length;}
    public static int get_vertical_edge_length(){return vertical_edge_length;}
}

//Via visitor, farmer feeds cattle
class Farmer implements Observer, Visitor {
    private Farm ownedFarm;
    private String farmerName;
    private Set<Cattle> outCattleList = new HashSet<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Farmer(String farmerName) {
        this.farmerName = farmerName;
        scheduler.scheduleAtFixedRate(this::feedAllCattleByTimer, 5, 15, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void notify(Cattle cattle) {
        boolean isCattleOut = cattle.getIsOut();
        System.out.println("----------------------");

        if (isCattleOut) {
            outCattleList.add(cattle);
            System.out.println("Cattle " + cattle.getEarTagUniqueId() + " is out now.");
        } else {
            outCattleList.remove(cattle);
            System.out.println("Cattle " + cattle.getEarTagUniqueId() + " is in now.");
        }

        //TODO HÜSEYİN, for loop ile yaz.
        String outCattleString = outCattleList.stream()
                .map(c -> String.valueOf(c.getEarTagUniqueId()))
                .collect(Collectors.joining(", "));

        System.out.println("Out cattle: " + outCattleString);
    }

    @Override
    public void visit(DairyCattle cattle) {
        AbstractFoodFactory foodFactory = new DairyCattleFoodFactory();
        cattle.eat(foodFactory);
    }

    @Override
    public void visit(BeefCattle cattle) {
        AbstractFoodFactory foodFactory = new MeatCattleFoodFactory();
        cattle.eat(foodFactory);
    }

    public void setOwnedFarm(Farm farm){
        this.ownedFarm = farm;
    }

    public void feedAllCattleByTimer(){
        ownedFarm.acceptVisitors(this);
    }
}

interface Observer{
    void notify(Cattle cattle);
}