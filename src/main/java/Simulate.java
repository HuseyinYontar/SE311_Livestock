import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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