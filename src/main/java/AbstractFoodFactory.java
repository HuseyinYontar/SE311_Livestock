abstract class Food {
    abstract String getNutritionInfo();
}

abstract class Protein extends Food {
}

abstract class Carbohydrate extends Food {
}

class Corn extends Carbohydrate {
    @Override
    public String getNutritionInfo() {
        return "gained 365 kcal, 9.4g protein, 74.3g carbs, 4.7g fat from Corn.      |";
    }
}

class Wheat extends Carbohydrate {
    @Override
    public String getNutritionInfo() {
        return "gained 340 kcal, 13.2g protein, 71.2g carbs, 2.5g fat from Wheat.    |";
    }
}

class Soybean extends Protein {
    @Override
    public String getNutritionInfo() {
        return "gained 446 kcal, 36.5g protein, 30.2g carbs, 19.9g fat from Soybean. |";
    }
}

class Canola extends Protein {
    @Override
    public String getNutritionInfo() {
        return "gained 375 kcal, 36g protein, 34g carbs, 3.5g fat from Canola.       |";
    }
}

public abstract class AbstractFoodFactory {
    /*
     * This java file uses the Abstract Factory Pattern to manage families of related objects—Protein and Carbohydrate
     * foods without specifying their concrete classes.
     *
     *   Hierarchy:
     * - Food (abstract): Base class defining common behavior for all foods.
     *   - Protein (abstract): Subclass for protein-rich foods.
     *       - Soybean, Canola (concrete classes)
     *   - Carbohydrate (abstract): Subclass for carbohydrate-rich foods.
     *       - Corn, Wheat (concrete classes)
     *
     * - AbstractFoodFactory (abstract): Declares creation methods for Protein and Carbohydrate.
     *   - DairyCattleFoodFactory, BeefCattleFoodFactory (concrete): Implement food creation related to cattle types.
     *
     */

    abstract Protein createProteinFood();

    abstract Carbohydrate createCarbohydrateFood();
}

class DairyCattleFoodFactory extends AbstractFoodFactory {
    @Override
    public Protein createProteinFood() {
        return new Soybean();
    }

    @Override
    public Carbohydrate createCarbohydrateFood() {
        return new Corn();
    }
}

class BeefCattleFoodFactory extends AbstractFoodFactory {
    @Override
    public Carbohydrate createCarbohydrateFood() {
        return new Wheat();
    }

    @Override
    public Protein createProteinFood() {
        return new Canola();
    }
}