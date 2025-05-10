import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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
    private ArrayList<Cattle> cattles;

    //TODO Hasan, location -> farmLocation isim değişikliği önerisi
    private Location location;
    public static final int horizontal_edge_length = 200;
    public static int vertical_edge_length = 200;

    public Farm(Farmer farmer){
        this.farmer = farmer;
        cattles = new ArrayList<>();
        location = new Location(0,0);
    }

    public void addCattle(Cattle cattle){
        cattles.add(cattle);
    }

    public void feedCattle(){
        for (Cattle c : cattles){
            c.accept(farmer);
        }
    }
}

//Via visitor, farmer feeds cattles


class Farmer implements Observer, Visitor {
    String farmerName;
    private Set<Cattle> outCattles = new HashSet<>();

    public Farmer(String farmerName) {
        this.farmerName = farmerName;
    }

    //TODO HASAN, synchronized ekledim önemli, hoca alametifarikasını sorabilir!
    @Override
    public synchronized void notify(Cattle cattle) {
        boolean isCattleOut = cattle.getIsOut();

        if (isCattleOut) {
            outCattles.add(cattle);
            System.out.println("Cattle " + cattle.getCattleId() + " is out now.");
        } else {
            outCattles.remove(cattle);
            System.out.println("Cattle " + cattle.getCattleId() + " is in now.");
        }

        String outCattlesString = outCattles.stream()
                .map(c -> String.valueOf(c.getCattleId()))
                .collect(Collectors.joining(", "));

        System.out.println("Out cattles: " + outCattlesString);
    }


    @Override
    public void visit(DairyCattle cattle) {
        AbstractFoodFactory foodFactory = new DairyCattleFoodFactory();
        cattle.eat(foodFactory.createProteinFood(), foodFactory.createCarbohydrateFood());
    }

    @Override
    public void visit(BeefCattle cattle) {
        AbstractFoodFactory foodFactory = new MeatCattleFoodFactory();
        cattle.eat(foodFactory.createProteinFood(), foodFactory.createCarbohydrateFood());

        //TODO: Hüseyin:: canerle visitor konuş, iki tip inek için bu değişmeli mi tartış.

    }
}
interface Observer{
    void notify(Cattle cattle);
}