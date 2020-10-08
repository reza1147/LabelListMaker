package DS;

import java.util.ArrayList;
import java.util.List;

public class GlassType {
    private String type1;
    private String type2;
    private int spacer;
    private boolean gaz;
    private List<Glass> list;

    public GlassType(String type1, String type2, int spacer, boolean gaz) {
        this.type1 = type1;
        this.type2 = type2;
        this.spacer = spacer;
        this.gaz = gaz;
        list = new ArrayList<>();
    }

    public List<Glass> getList() {
        return list;
    }

    public void setList(List<Glass> list) {
        this.list = list;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getSpacer() {
        return spacer;
    }

    public void setSpacer(int spacer) {
        this.spacer = spacer;
    }

    public boolean getGaz() {
        return gaz;
    }

    public void setGaz(boolean gaz) {
        this.gaz = gaz;
    }

    @Override
    public String toString() {
        return type1 + ":" + type2 + ":" + spacer + ":" + (gaz ? "Argon" : "none") + "\n" + list;

    }

    public int getTedad() {
        int t = 0;
        for (Glass lp : list)
            t += lp.getC();
        return t;
    }

    public double getMetrazh() {
        double m = 0;
        for (Glass lp : list)
            m += lp.getH() * lp.getW() * lp.getC();
        return m;
    }
}
