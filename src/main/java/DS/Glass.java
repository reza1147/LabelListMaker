package DS;

public class Glass {
    private double w;
    private double h;
    private int c;
    private String code;

    public Glass(double w, double h, int c, String code) {
        this.w = w;
        this.h = h;
        this.c = c;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return w + " x " + h + " _ " + c;
    }
}
