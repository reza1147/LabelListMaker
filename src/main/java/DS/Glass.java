package DS;

public class Glass {
    private double w;
    private double h;
    private int c;

    public Glass(double w, double h, int c) {
        this.w = w;
        this.h = h;
        this.c = c;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return w + " x " + h + " _ " + c;
    }
}
