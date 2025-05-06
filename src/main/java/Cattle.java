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
        return true;
    }

    @Override
    public String checkHealth() {
       return "Healthy";
    }


}

class BeefCattle extends Cattle{
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean checkBarcode() {
        return true;
    }

    @Override
    public String checkHealth() {
        return "Moderate";
    }


}