public interface Visitor{
    void visit(DairyCattle cattle);
    void visit(BeefCattle cattle);
}

class MinistryInspectorVisitor implements Visitor{

    @Override
    public void visit(DairyCattle cattle) {
       boolean barcode = cattle.getBarcode();
       if(barcode){
        System.out.println(cattle.getCattleId() + " has barcode." );
       }else{
           System.out.println(cattle.getCattleId() + " does not have barcode.");
       }
    }

    @Override
    public void visit(BeefCattle cattle) {
        boolean barcode = cattle.getBarcode();
        if (barcode) {
            System.out.println(cattle.getCattleId() + " has barcode.");
        } else {
            System.out.println(cattle.getCattleId() + " does not have barcode.");
        }
    }

}

class VeterinarianVisitor implements Visitor{
    @Override
    public void visit(DairyCattle cattle) {
        String status = cattle.getHealth();
        System.out.println("DairyCattle: " +cattle.getCattleId() + " status:  " + status);
    }

    @Override
    public void visit(BeefCattle cattle) {
        String status = cattle.getHealth();
        System.out.println("BeefCattle: " +cattle.getCattleId() + " status:  " + status);
    }
}
