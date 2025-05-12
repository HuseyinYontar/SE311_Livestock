public abstract class Cattle {
    private static int cattleCounter = 0;
    private int earTagUniqueId = 0;
    private Observer observer;
    private boolean isOut;
    private LocationDevice device;

    public Cattle() {
        // Zigbee device with a probability %80.
        device = Randomizer.getRandomizedValue_0_to_10() <= 8 ? new ZigbeeDevice(this) : new BluetoothDevice(this);
        this.earTagUniqueId = ++cattleCounter;
    }

    /**
     * @param v It is one of the methods in visitor pattern. Allows visitor to do their job. {v.visit(this)};
     */
    public abstract void accept(Visitor v);

    public abstract String getHealth();

    /**
     * Invokes notify method of observer, when the cattle itself is not in the farm.
     */
    public void notifyObserver() {
        if (observer == null) {
            System.out.print("Observer haven't set yet for cattleId:" + earTagUniqueId);
            return;
        }
        observer.update(this);
    }

    /**
     * @param foodFactory Dynamically generates both protein and carbohydrate food for the cattle.
     */
    public void eat(AbstractFoodFactory foodFactory) {
        Protein protein = foodFactory.createProteinFood();
        Carbohydrate carbohydrate = foodFactory.createCarbohydrateFood();

        System.out.println("| Cattle " + earTagUniqueId + " " + protein.getNutritionInfo());
        System.out.println("| Cattle " + earTagUniqueId + " " + carbohydrate.getNutritionInfo());
    }

    public int getEarTagUniqueId() {
        return earTagUniqueId;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Boolean getIsOut() {
        return isOut;
    }

    public void setIsOut(Boolean isOut) {
        this.isOut = isOut;
    }
}

class DairyCattle extends Cattle {
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String getHealth() {
        return "Healthy";
    }
}

class BeefCattle extends Cattle {
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String getHealth() {
        return "Moderate";
    }
}