public class Location {
    private int x_axis;
    private int y_axis;

    public Location(int x_axis, int y_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
    }

    public int[] getLocation(){
        return new int[]{x_axis,y_axis};
    }
}
