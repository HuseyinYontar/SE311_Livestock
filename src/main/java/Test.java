import java.util.concurrent.CountDownLatch;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        // Çiftlik oluştur
        Farm farm = new Farm();

        // Çiftçi oluştur
        Farmer farmer = new Farmer("Ali Çiftçi");

        // Hayvanları oluştur
        DairyCattle cattle1 = new DairyCattle();
        BeefCattle cattle2 = new BeefCattle();
        DairyCattle cattle3 = new DairyCattle();
        DairyCattle cattle4 = new DairyCattle();
        BeefCattle cattle5 = new BeefCattle();
        DairyCattle cattle6 = new DairyCattle();
        DairyCattle cattle7 = new DairyCattle();
        BeefCattle cattle8 = new BeefCattle();
        DairyCattle cattle9 = new DairyCattle();


        // Hayvanları çiftliğe ekle
        farm.addCattle(cattle1);
        farm.addCattle(cattle2);
        farm.addCattle(cattle3);
        farm.addCattle(cattle4);
        farm.addCattle(cattle5);
        farm.addCattle(cattle6);
        farm.addCattle(cattle7);
        farm.addCattle(cattle8);
        farm.addCattle(cattle9);

        // Çiftçiyi her hayvanın gözlemcisi olarak ekle
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
