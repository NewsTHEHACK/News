package object;

public class News_Point {
    private double startx,endy,starty,endx;
    public String tag,id;
    private static double cri=0.5;

    public News_Point(double startx, double starty,double endx,double endy) {
        this.startx = startx;
        this.endx = endx;
        this.starty=starty;
        this.endy=endy;

    }

    public double getStartx() {
        return startx;
    }

    public void setStartx(double startx) {
        this.startx = startx;
    }

    public double getEndy() {
        return endy;
    }

    public void setEndy(double endy) {
        this.endy = endy;
    }

    public double getStarty() {
        return starty;
    }

    public void setStarty(double starty) {
        this.starty = starty;
    }

    public double getEndx() {
        return endx;
    }

    public void setEndx(double endx) {
        this.endx = endx;
    }

    public boolean isin(double x, double y){
        double midpx=(startx+endx)/2;
        double midpy=(starty+endy)/2;
        if (Math.abs(midpx-x)<cri&&Math.abs(midpy-y)<cri){
            return true;
        }
        return false;
    }
}
