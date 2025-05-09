import java.util.concurrent.CountDownLatch;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Farm farm = new Farm();
        Farmer farmer = new Farmer("HASAN");

        //TODO HASAN 30 tane inek oluşturan bi factory method olabilir, farm.createCattle(Factory beefCattle, 15) 15 tane oluşturur
        //TODO HASAN 30 tane inek oluşturan bi factory method olabilir, farm.createCattle(Factory dairyCattle) 14 tane oluşturur
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

        //TODO HASAN observer için basit bi metod yazabilriz tek tek ayarlamamak için farm->setFarmObserver(farmer)
        cattle1.setObserver(farmer);
        cattle2.setObserver(farmer);
        cattle3.setObserver(farmer);
        cattle4.setObserver(farmer);
        cattle5.setObserver(farmer);
        cattle6.setObserver(farmer);
        cattle7.setObserver(farmer);
        cattle8.setObserver(farmer);
        cattle9.setObserver(farmer);

        new CountDownLatch(1).await();
    }

    // Hayvan hareketlerini simüle eden metot
    private static void simulateMovement(Cattle cattle, boolean goOutside) {
        // Gerçek bir uygulamada bu, cihazdan gelen konum verileriyle yapılırdı
        // Burada sadece örnek olarak doğrudan durum değişikliği yapıyoruz
        cattle.setIsOut(goOutside);
    }
}
