import java.util.ArrayList;

public abstract class Cattle{
    protected int cattleId;
    public abstract void accept(Visitor v);
    public abstract boolean checkBarcode();
    public abstract String checkHealth();
    public int getCattleId(){
        return cattleId;
    }
  
    private int cattleID;
    private boolean in_out_state;
    private LocationDevice device;
    // private Observer observer;
    public Cattle(){
        // Zigbee device with a probability %80.
        device = Randomizer.getRandomizedValue_0_to_10() <= 8 ? new ZigbeeDevice(this) : new BluetoothDevice(this);
    }

    public int getCattleID() {
        return cattleID;
    }

    public void setIn_out_state(boolean state){
        in_out_state = state;

    }
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
