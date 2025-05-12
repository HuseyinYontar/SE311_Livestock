public interface Visitor {
    void visit(DairyCattle cattle);

    void visit(BeefCattle cattle);
}

class MinistryInspectorVisitor implements Visitor {
    // Ministry Inspector visits the farm to check whether the cattle has unique barcode or not.

    @Override
    public void visit(DairyCattle cattle) {
        if (cattle.getEarTagUniqueId() != 0) {
            System.out.println("Ministry Inspector: Dairy Cattle " + cattle.getEarTagUniqueId() + " has barcode.");
        } else {
            System.out.println("Ministry Inspector: Dairy Cattle " + cattle.getEarTagUniqueId() + " does not have barcode.");
        }
    }

    @Override
    public void visit(BeefCattle cattle) {
        if (cattle.getEarTagUniqueId() != 0) {
            System.out.println("Ministry Inspector: Beef Cattle "+cattle.getEarTagUniqueId() + " has barcode.");
        } else {
            System.out.println("Ministry Inspector: Beef Cattle "+cattle.getEarTagUniqueId() + " does not have barcode.");
        }
    }
}

class VeterinarianVisitor implements Visitor {
    // Veterinarian visits the farm to vaccinate all the cattle.

    @Override
    public void visit(DairyCattle cattle) {
        String status = cattle.getHealth();
        System.out.println("Veterinarian: Dairy Cattle: " + cattle.getEarTagUniqueId() + " status:  " + status + ". Vaccinating...");
    }

    @Override
    public void visit(BeefCattle cattle) {
        String status = cattle.getHealth();
        System.out.println("Veterinarian: Beef Cattle: " + cattle.getEarTagUniqueId() + " status:  " + status + ". Vaccinating...");
    }
}
