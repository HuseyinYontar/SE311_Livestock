import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Farm{
//          It is assumed that the boundaries are:
//          -100 <= x <= 100 and -100 <= y <= 100
//            +----------200-----------+
//            |                        |
//            |                        |
//            |          FARM          |
//           200          x            |
//            |                        |
//            |                        |
//            |                        |
//            +------------------------+

    //private Farmer farmer;
    private ArrayList<Cattle> cattles;

    //TODO Hasan, location -> farmLocation
    private Location location;
    public static final int horizontal_edge_length = 200;
    public static int vertical_edge_length = 200;

    public Farm(){
        cattles = new ArrayList<>();
        location = new Location(0,0);
    }

    public Location getFarmLocation() {
        return location;
    }

    public void addCattle(Cattle cattle){
        cattles.add(cattle);
    }
}

class Farmer implements Observer {
    String farmerName;
    private Set<Cattle> outCattles = new HashSet<>();

    public Farmer(String farmerName){
        this.farmerName = farmerName;
    }

    @Override
    public void notify(Cattle cattle){
        boolean isCattleOut = cattle.getIsOut();

        if (isCattleOut) {
            outCattles.add(cattle);
            System.out.println("Cattle " + cattle.getCattleId() + " is out now.");
        } else {
            outCattles.removeIf(c -> c.getCattleId() == cattle.getCattleId()); // güvenli çıkarma
            System.out.println("Cattle " + cattle.getCattleId() + " is in now.");
        }

        String outCattlesString = outCattles.stream()
                .map(c -> "" + c.getCattleId())
                .collect(Collectors.joining(", "));

        System.out.println("Out cattles: "+ outCattlesString);
    }
}

interface Observer{
    void notify(Cattle cattle);
}