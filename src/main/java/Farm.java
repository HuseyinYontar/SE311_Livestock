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

class Simulate {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws InterruptedException {
        printArt();
        
        Farm farm = new Farm();
        Farmer farmer = new Farmer("Bob");
        farm.addFarmer(farmer);
        MinistryInspectorVisitor visitorMinistry = new MinistryInspectorVisitor();
        VeterinarianVisitor visitorVeterinarian = new VeterinarianVisitor();


        DairyCattle cattle1 = new DairyCattle();
        BeefCattle cattle2 = new BeefCattle();
        DairyCattle cattle3 = new DairyCattle();
        BeefCattle cattle4 = new BeefCattle();
        DairyCattle cattle5 = new DairyCattle();
        BeefCattle cattle6 = new BeefCattle();
        DairyCattle cattle7 = new DairyCattle();
        BeefCattle cattle8 = new BeefCattle();
        DairyCattle cattle9 = new DairyCattle();
        BeefCattle cattle10 = new BeefCattle();
        DairyCattle cattle11 = new DairyCattle();
        BeefCattle cattle12 = new BeefCattle();

        farm.addCattle(cattle1);
        farm.addCattle(cattle2);
        farm.addCattle(cattle3);
        farm.addCattle(cattle4);
        farm.addCattle(cattle5);
        farm.addCattle(cattle6);
        farm.addCattle(cattle7);
        farm.addCattle(cattle8);
        farm.addCattle(cattle9);
        farm.addCattle(cattle10);
        farm.addCattle(cattle11);
        farm.addCattle(cattle12);

        farm.setObserverForAllCattle(farmer);

        scheduler.scheduleAtFixedRate(() -> ministryVisitorHelper(farm, visitorMinistry), 15, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> veterinarianVisitorHelper(farm, visitorVeterinarian), 10, 25, TimeUnit.SECONDS);

    }
    public static void ministryVisitorHelper(Farm farm, MinistryInspectorVisitor visitor){
        System.out.println("-----------------------Ministry Inspector visits the farm-----------------------");
        farm.acceptVisitors(visitor);
        System.out.println("--------------------------------------------------------------------------------");
    }
    public static void veterinarianVisitorHelper(Farm farm, VeterinarianVisitor visitor){
        System.out.println("--------------------------Veterinarian visits the farm--------------------------");
        farm.acceptVisitors(visitor);
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void printArt(){
        System.out.println("-----------------------------------------Welcome to our farm-----------------------------------------");
        System.out.println("---------------------------------------------Prepared by---------------------------------------------");

        String cattle = """
                              (__)                    (__)                    (__)                    (__)
                      `\\------(oo)            `\\------(oo)            `\\------(oo)            `\\------(oo)
                        ||    (__)              ||    (__)              ||    (__)              ||    (__)
                        ||w--||                 ||w--||                 ||w--||                 ||w--||        
                                                                                               
                        Hasan                    Caner                   Defne                  Hüseyin
                """;
        System.out.print(cattle);
        System.out.println("-----------------------------------------------------------------------------------------------------");
    }
}