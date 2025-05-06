public abstract class Cattle {
    protected int cattleId;
    public abstract void accept(Visitor v);
    public abstract boolean checkBarcode();
    public abstract String checkHealth();
    public int getCattleId(){
        return cattleId;
    }
}

class DairyCattle extends Cattle{
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean checkBarcode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkBarcode'");
    }

    @Override
    public String checkHealth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkHealth'");
    }


}

class BeefCattle extends Cattle{
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean checkBarcode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkBarcode'");
    }

    @Override
    public String checkHealth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkHealth'");
    }


}