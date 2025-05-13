// HASAN BASRİ KARSLIOĞLU
// CANERCAN DEMİR
// DEFNE YILMAZ
// HÜSEYİN YONTAR
// Project#3 Livestock

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class LocationDevice {
    private Cattle ownerCattle;
    private Location currentLocation;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public LocationDevice(Cattle ownerCattle) {
        this.ownerCattle = ownerCattle;
        this.currentLocation = Randomizer.getRandomLocation();
        // the cattle's location is updated every second. It was set to 1097 milliseconds
        // in order to prevent intervening the print statements of each other.
        scheduler.scheduleAtFixedRate(this::updateLocationEverySecond, 1097, 1097, TimeUnit.MILLISECONDS);

        // this scheduler sends location info every 5 seconds to the server.
        scheduler.scheduleAtFixedRate(this::sendSignal, 7500, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * Each device sends signal to the database.
     */
    abstract void sendSignal();

    /**
     * Updates cattle's location every second by generating random x and y values. If the cattle exits the
     * farm boundary the observer is notified.
     */
    private void updateLocationEverySecond() {
        boolean wasOut = ownerCattle.getIsOut();

        currentLocation.updateLocation(Randomizer.getRandomUpdateValue());
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
        int[] current_location_values = currentLocation.getLocation();
        return Math.abs(current_location_values[0] - Farm.getFarmLocation().getLocation()[0]) > 100
                || Math.abs(current_location_values[1] - Farm.getFarmLocation().getLocation()[1]) > 100;
    }

    public Cattle getOwnerCattle() {
        return ownerCattle;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}

class ZigbeeDevice extends LocationDevice {
    public ZigbeeDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {
        ZigbeeSignal signal = new ZigbeeSignal(getOwnerCattle().getEarTagUniqueId(), getCurrentLocation());
        FarmDatabase.getInstance().updateCattleLocation(signal.sendZigbeeSignal());
    }
}

class BluetoothDevice extends LocationDevice {
    public BluetoothDevice(Cattle cattle) {
        super(cattle);
    }

    @Override
    void sendSignal() {
        BluetoothSignal bluetoothSignal = new BluetoothSignal(getOwnerCattle().getEarTagUniqueId(), getCurrentLocation());
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

    /**
     * @return ZigbeeSignal when it is invoked by Zigbee devices.
     */
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

    /**
     * @return BluetoothSignal when it is invoked.
     */
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

    /**
     * @return ZigbeeSignal when it is invoked. The method adapts Bluetooth signals to Zigbee signals.
     */
    public ZigbeeSignal sendZigbeeSignal() {
        System.out.println("Adapting Bluetooth signal to Zigbee...   ↻");
        setEarTagUniqueId(bluetoothSignal.getEarTagUniqueId());
        setCurrentLocation(bluetoothSignal.getCurrentLocation());
        System.out.println("Adapted signal sent to server as Zigbee. ↗");
        return this; //TODO: Caner cihaz kendini gönderir mi? sorusunu sordu discuss etmeli.
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


    /**
     * @param values Updates cattle's location by using the random location parameter values.
     */
    public void updateLocation(int[] values) {
        x_axis = x_axis + values[0];
        y_axis = y_axis + values[1];
    }

    public String toString() {
        return "location: x=" + x_axis + " y=" + y_axis;
    }
}

class Randomizer {
    // Utility class to provide random values for different cases.

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