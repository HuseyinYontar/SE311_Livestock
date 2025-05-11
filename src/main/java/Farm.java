import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    public void addFarmer(Farmer farmer) {
        this.farmer = farmer;
        farmer.setOwnedFarm(this);
    }

    public void addCattle(Cattle cattle) {
        cattleList.add(cattle);
    }

    public void setAllCattleObservers(Observer observer) {
        for (Cattle cattle : cattleList) {
            cattle.setObserver(observer);
        }
    }

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

    @Override
    public synchronized void notify(Cattle cattle) {
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

    public void setOwnedFarm(Farm farm) {
        this.ownedFarm = farm;
    }

    public void feedAllCattleByTimer() {
        System.out.println("----Feeding All Cattle-----------------------------------------------------------");
        ownedFarm.acceptVisitors(this);
        System.out.println("---------------------------------------------------------------------------------");
    }
}

interface Observer {
    void notify(Cattle cattle);
}

class Test{
    public static void main(String[] args) throws InterruptedException {
        Farm farm = new Farm();
        Farmer farmer = new Farmer("BALIKCI HASAN");
        farm.addFarmer(farmer);
        MinistryInspectorVisitor visitorMinistry = new MinistryInspectorVisitor();
        VeterinarianVisitor visitorVeterinarian = new VeterinarianVisitor();


        DairyCattle cattle1 = new DairyCattle();
        BeefCattle cattle2 = new BeefCattle();
        DairyCattle cattle3 = new DairyCattle();
        DairyCattle cattle4 = new DairyCattle();
        BeefCattle cattle5 = new BeefCattle();
        DairyCattle cattle6 = new DairyCattle();
        DairyCattle cattle7 = new DairyCattle();
        BeefCattle cattle8 = new BeefCattle();
        DairyCattle cattle9 = new DairyCattle();

        farm.addCattle(cattle1);
        farm.addCattle(cattle2);
        farm.addCattle(cattle3);
        farm.addCattle(cattle4);
        farm.addCattle(cattle5);
        farm.addCattle(cattle6);
        farm.addCattle(cattle7);
        farm.addCattle(cattle8);
        farm.addCattle(cattle9);

        farm.setAllCattleObservers(farmer);

        Thread.sleep(11000);
        seasonalVisitorHelper(farm,visitorMinistry);
        seasonalVisitorHelper(farm,visitorVeterinarian);
    }
    public static void seasonalVisitorHelper(Farm farm, Visitor visitor){
        farm.acceptVisitors(visitor);
    }
}