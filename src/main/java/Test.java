public class Test {
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
    }
}
