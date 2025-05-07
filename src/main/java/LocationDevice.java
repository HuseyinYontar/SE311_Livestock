import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class LocationDevice {
    protected Cattle cattle;
    protected Location current_location;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public LocationDevice(Cattle cattle){
        this.cattle = cattle;
        this.current_location = Randomizer.getRandomLocation();

        scheduler.scheduleAtFixedRate(this::updateLocation, 0, 1, TimeUnit.SECONDS);
    }

    abstract void sendSignal();//?? idk

    public void updateLocation(){
        current_location.updateLocation(Randomizer.getRandomUpdateValue());
    }

    private void calculate_distance_to_farm(){
        // Burayı nasıl yapmam gerektiğinden emin değilim. Cattlelarda farm tutmamız gerekiyor ki
        // farmın locationını alabilelim, ya da farmda locationları public olarak diğerlerine yaycaz ki kullanabilelim.
        // Bu fonksiyon daha sonra observer patternda farmın içinde mi değil mi diye kullanılabilir. şu anki konumunun
        // x ve y deki mutlak değerleri 100den büyükse observer notify edilir.
    }
}

class ZigbeeDevice extends LocationDevice{

    public ZigbeeDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {

    }
}

class BluetoothDevice extends LocationDevice{

    public BluetoothDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {

    }
}

class ZigbeeSignal{

}

class BluetoothSignal{

}

class BluetoothToZigbeeAdapter extends ZigbeeSignal{
    private BluetoothSignal bluetoothSignal;

    public BluetoothToZigbeeAdapter(BluetoothSignal bSignal){
        bluetoothSignal = bSignal;
    }
}

class Location {
    private int x_axis;
    private int y_axis;

    public Location(int x_axis, int y_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
    }

    public int[] getLocation(){
        return new int[]{x_axis,y_axis};
    }

    public void updateLocation(int[] values){
        x_axis = x_axis + values[0];
        y_axis = y_axis + values[1];
    }
}

class Randomizer{
    private static Random rd = new Random(System.currentTimeMillis());
    public static Location getRandomLocation(){
        return new Location(rd.nextInt(-100,101), rd.nextInt(-100,101));
    }
    public static int[] getRandomUpdateValue(){
        return new int[]{rd.nextInt(-5,5), rd.nextInt(-5,5)};
    }
    public static int getRandomizedValue_0_to_10(){
        return rd.nextInt(1,11);
    }
}