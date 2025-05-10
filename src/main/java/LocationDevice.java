import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class LocationDevice {
    //TODO HASAN, cattle yerine owner diyebiliriz
    protected Cattle cattle;
    protected Location current_location;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public LocationDevice(Cattle cattle) {
        this.cattle = cattle;
        this.current_location = Randomizer.getRandomLocation();
        // locationa göre ineğin boolean state'i setlenmeli.

        // TODO HASAN, updatenin tetiklenmesinin ana bir merkezden olması mı yoksa her devicenin kendisini tetiklemesi konusunu raporda belirt!
        scheduler.scheduleAtFixedRate(this::updateLocationEverySecond, 5, 1, TimeUnit.SECONDS);
    }

    abstract void sendSignal();//?? idk

    private void updateLocationEverySecond() {
        boolean wasOut = cattle.getIsOut();

        current_location.updateLocation(Randomizer.getRandomUpdateValue());
        boolean isNowOut = calculate_cattle_in_or_out();

        if (wasOut != isNowOut) {
            cattle.setIsOut(isNowOut);
            cattle.notifyObserver();
        }
    }

    /**
     * @return true if the cattle in the farm's boundaries, false if not.
     */
    private boolean calculate_cattle_in_or_out() {
        // Burayı nasıl yapmam gerektiğinden emin değilim. Cattlelarda farm tutmamız gerekiyor ki
        // farmın locationını alabilelim, ya da farmda locationları public olarak diğerlerine yaycaz ki kullanabilelim.
        // Bu fonksiyon daha sonra observer patternda farmın içinde mi değil mi diye kullanılabilir. şu anki konumunun
        // x ve y deki mutlak değerleri 100den büyükse observer notify edilir.
        int[] current_location_values = current_location.getLocation();
        return Math.abs(current_location_values[0] ) > Farm.horizontal_edge_length / 2
                || Math.abs(current_location_values[1]) > Farm.vertical_edge_length / 2;
    }


}

class ZigbeeDevice extends LocationDevice {
    public ZigbeeDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {
        ZigbeeSignal signal = new ZigbeeSignal();
        signal.sendToServer();
    }
}

class BluetoothDevice extends LocationDevice {
    public BluetoothDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {
        BluetoothSignal bluetoothSignal = new BluetoothSignal();
        BluetoothToZigbeeAdapter adapter = new BluetoothToZigbeeAdapter(bluetoothSignal);
        adapter.sendToServer();
    }
}

class ZigbeeSignal {
    void sendToServer() {
        System.out.println("Zigbee signal sent to server.");
    }
}

class BluetoothSignal {
    public void sendRaw() {
        System.out.println("Bluetooth signal sent (raw).");
    }
}

class BluetoothToZigbeeAdapter extends ZigbeeSignal {
    private BluetoothSignal bluetoothSignal;

    public BluetoothToZigbeeAdapter(BluetoothSignal bSignal) {
        bluetoothSignal = bSignal;
    }

    void sendToServer() {
        System.out.println("Adapting Bluetooth signal to Zigbee...");
        bluetoothSignal.sendRaw();
        System.out.println("Adapted signal sent to server as Zigbee.");
    }
}

class Location {
    private int x_axis;
    private int y_axis;

    public Location(int x_axis, int y_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
    }

    public int[] getLocation() {
        return new int[]{x_axis, y_axis};
    }

    //TODO HASAN, updateLocation yerine addLocation daha mantıklı olabilir.
    public void updateLocation(int[] values) {
        x_axis = x_axis + values[0];
        y_axis = y_axis + values[1];
    }
}

class Randomizer {
    private static Random rd = new Random(System.currentTimeMillis());

    public static Location getRandomLocation() {
        return new Location(rd.nextInt(-100, 101), rd.nextInt(-100, 101));
    }

    public static int[] getRandomUpdateValue() {
        return new int[]{rd.nextInt(-20, 20), rd.nextInt(-5, 5)};
    }

    public static int getRandomizedValue_0_to_10() {
        return rd.nextInt(1, 11);
    }
}