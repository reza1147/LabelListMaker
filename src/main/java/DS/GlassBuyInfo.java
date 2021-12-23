package DS;

import DB.MyDate;

import java.util.ArrayList;
import java.util.List;

public class GlassBuyInfo {
    private String customer;
    private MyDate date;
    private List<GlassType> list;
    private  Integer ID;
    private Boolean hasCode;

    public Boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(Boolean hasCode) {
        this.hasCode = hasCode;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public GlassBuyInfo(String customer, MyDate date,Boolean hasCode ) {
        this.customer = customer;
        this.date = date;
        this.hasCode = hasCode;
        list = new ArrayList<>();
    }

    public List<GlassType> getList() {
        return list;
    }

    public void setList(List<GlassType> list) {
        this.list = list;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public MyDate getDate() {
        return date;
    }

    public void setDate(MyDate date) {
        this.date = date;
    }

    public Integer getTedad() {
        int t = 0;
        for (GlassType lp : list)
            t += lp.getTedad();
        return t;
    }

    public Double getMetrazh() {
        double m = 0;
        for (GlassType lp : list)
            m += lp.getMetrazh();
        return m;
    }

    @Override
    public String toString() {
        return customer + ":" + date + "\n" + list;
    }
}
