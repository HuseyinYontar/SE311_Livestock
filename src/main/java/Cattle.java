import java.util.ArrayList;

public abstract class Cattle{
    private static int idCounter = 0;
    protected int cattleId;
    public abstract void accept(Visitor v);
    public abstract boolean checkBarcode();
    public abstract String checkHealth();
    private Observer observer;

    //TODO HASAN move that method to the end of the file under getter setters section
    public int getCattleId(){return cattleId;}
    private boolean isOut;
    private LocationDevice device;
    // private Observer observer;
    public Cattle(){
        // Zigbee device with a probability %80.
        device = Randomizer.getRandomizedValue_0_to_10() <= 8 ? new ZigbeeDevice(this) : new BluetoothDevice(this);
        this.cattleId = ++idCounter;
    }

    public void notifyObserver(){
        if (observer == null){
            System.out.print("Observer haven't set yer for cattleId:" + cattleId);
            return;
        }
        observer.notify(this);
    }
    public void setObserver(Observer observer){this.observer = observer;};
    public Boolean getIsOut(){return isOut;}
    public void setIsOut(Boolean isOut){this.isOut = isOut;}
}

class DairyCattle extends Cattle{
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean checkBarcode() {
        return true;
    }

    @Override
    public String checkHealth() {
       return "Healthy";
    }

  
}

class BeefCattle extends Cattle{
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean checkBarcode() {
        return true;
    }

    @Override
    public String checkHealth() {
        return "Moderate";
    }


}
