import java.util.ArrayList;

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
    private Location location;
    private final int horizontal_edge_length = 200;
    private final int vertical_edge_length = 200;

    public Farm(){
        cattles = new ArrayList<>();
        location = new Location(0,0);
    }
}

class Farmer{

}