interface Visitor{
    void visit(Cattle cattle);
}

class MinistryInspectorVisitor implements Visitor{

    @Override
    public void visit(Cattle cattle) {
       boolean barcode = cattle.checkBarcode();
       if(barcode){
        System.out.println(cattle.getCattleId() + " has barcode." );
       }else{
           System.out.println(cattle.getCattleId() + " does not have barcode.");
       }
    }

}

class VeterinarianVisitor implements Visitor{
    @Override
    public void visit(Cattle cattle) {
        String status = cattle.checkHealth();
        System.out.println(cattle.getCattleId() + " status:  " + status);
    }
}
