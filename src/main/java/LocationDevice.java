import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class LocationDevice {
    protected Cattle ownerCattle;
    protected Location current_location;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public LocationDevice(Cattle ownerCattle) {
        this.ownerCattle = ownerCattle;
        this.current_location = Randomizer.getRandomLocation();
        scheduler.scheduleAtFixedRate(this::updateLocationEverySecond, 1097, 1097, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::sendSignal, 7500, 5000, TimeUnit.MILLISECONDS);
    }

    abstract void sendSignal();

    private void updateLocationEverySecond() {
        boolean wasOut = ownerCattle.getIsOut();

        current_location.updateLocation(Randomizer.getRandomUpdateValue());
        boolean isNowOut = calculate_cattle_in_or_out();

        if (wasOut != isNowOut) {
            ownerCattle.setIsOut(isNowOut);
            ownerCattle.notifyObserver();
        }
    }

    /**
     * @return true if the cattle in the farm's boundaries, false if not.
     */
    private boolean calculate_cattle_in_or_out() {
        int[] current_location_values = current_location.getLocation();
        return Math.abs(current_location_values[0] - Farm.getFarmLocation().getLocation()[0]) > 100
                || Math.abs(current_location_values[1] - Farm.getFarmLocation().getLocation()[1]) > 100;
    }

}

class ZigbeeDevice extends LocationDevice {
    public ZigbeeDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {
        ZigbeeSignal signal = new ZigbeeSignal(ownerCattle.getEarTagUniqueId(), current_location);
        FarmDatabase.getInstance().updateCattleLocation(signal.sendZigbeeSignal());
    }
}

class BluetoothDevice extends LocationDevice {
    public BluetoothDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {
        BluetoothSignal bluetoothSignal = new BluetoothSignal(ownerCattle.getEarTagUniqueId(), current_location);
        BluetoothToZigbeeAdapter adapter = new BluetoothToZigbeeAdapter(bluetoothSignal);
        FarmDatabase.getInstance().updateCattleLocation(adapter.sendZigbeeSignal());
    }
}

class ZigbeeSignal {
    private int earTagUniqueId;
    private Location currentLocation;

    public ZigbeeSignal(int earTagUniqueId, Location currentLocation) {
        this.earTagUniqueId = earTagUniqueId;
        this.currentLocation = currentLocation;
    }

    public ZigbeeSignal() {
    }

    public ZigbeeSignal sendZigbeeSignal() {
        System.out.println("Zigbee signal sent to server. ↗");
        return this;
    }

    public int getEarTagUniqueId() {
        return earTagUniqueId;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setEarTagUniqueId(int earTagUniqueId) {
        this.earTagUniqueId = earTagUniqueId;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}

class BluetoothSignal {
    private int earTagUniqueId;
    private Location currentLocation;

    public BluetoothSignal(int earTagUniqueId, Location currentLocation) {
        this.earTagUniqueId = earTagUniqueId;
        this.currentLocation = currentLocation;
    }

    public BluetoothSignal sendBluetoothSignal() {
        System.out.println("Bluetooth signal sent to adapter.        ⤵");
        return this;
    }

    public int getEarTagUniqueId() {
        return earTagUniqueId;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}

class BluetoothToZigbeeAdapter extends ZigbeeSignal {
    private BluetoothSignal bluetoothSignal;

    public BluetoothToZigbeeAdapter(BluetoothSignal bSignal) {
        super();
        bluetoothSignal = bSignal.sendBluetoothSignal();
    }

    public ZigbeeSignal sendZigbeeSignal() {
        System.out.println("Adapting Bluetooth signal to Zigbee...   ↕");
        setEarTagUniqueId(bluetoothSignal.getEarTagUniqueId());
        setCurrentLocation(bluetoothSignal.getCurrentLocation());
        System.out.println("Adapted signal sent to server as Zigbee. ↗");
        return this;
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

    public String toString() {
        return "location: x=" + x_axis + " y=" + y_axis;
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