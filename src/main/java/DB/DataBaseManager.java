package DB;

import DS.Glass;
import DS.GlassBuyInfo;
import DS.GlassType;
import com.healthmarketscience.jackcess.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by rezat on 20/09/2017.
 */
public class DataBaseManager {
    private Database db;
    private Table printList;
    private Table OrderList;
    private Table SizeList;
    private Table TypeList;

    public DataBaseManager(String homeDir) {
        if (dataBaseExists(homeDir)) {
            openDataBase(homeDir);
        } else {
            makeDataBase(homeDir);
        }
    }

    private boolean dataBaseExists(String homeDir) {
        return new File(homeDir + "db.mdb").exists();
    }

    private void makeDataBase(String homeDir) {
        try {
            db = new DatabaseBuilder(new File(homeDir + "db.mdb"))
                    .setFileFormat(Database.FileFormat.V2010)
                    .create();
            OrderList = new TableBuilder("OrderList")
                    .addColumn(new ColumnBuilder("OrderID", DataType.LONG).setAutoNumber(true))
                    .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME)
                            .addColumns("OrderID")
                            .setPrimaryKey()
                            .setRequired())
                    .addColumn(new ColumnBuilder("Name", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Date", DataType.TEXT))
                    .addColumn(new ColumnBuilder("hasCode", DataType.BOOLEAN))
                    .addColumn(new ColumnBuilder("metrazh", DataType.DOUBLE))
                    .addColumn(new ColumnBuilder("tedad", DataType.INT))
                    .toTable(db);


            TypeList = new TableBuilder("TypeList")
                    .addColumn(new ColumnBuilder("OrderID-FK", DataType.LONG))
                    .addColumn(new ColumnBuilder("TypeID", DataType.LONG))
                    .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME)
                            .addColumns("OrderID-FK")
                            .addColumns("TypeID")
                            .setPrimaryKey()
                            .setRequired())
                    .addColumn(new ColumnBuilder("Shishe1", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Shishe2", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Spacer", DataType.INT))
                    .addColumn(new ColumnBuilder("gaz", DataType.BOOLEAN))
                    .toTable(db);

            Relationship rel1 = new RelationshipBuilder("OrderList", "TypeList")
                    .addColumns("OrderID", "OrderID-FK")
                    .setReferentialIntegrity()
                    .setCascadeDeletes()
                    .toRelationship(db);

            SizeList = new TableBuilder("SizeList")
                    .addColumn(new ColumnBuilder("OrderID-FK", DataType.LONG))
                    .addColumn(new ColumnBuilder("TypeID-FK", DataType.LONG))
                    .addColumn(new ColumnBuilder("SizeID", DataType.LONG))
                    .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME)
                            .addColumns("OrderID-FK")
                            .addColumns("TypeID-FK")
                            .addColumns("SizeID")
                            .setPrimaryKey()
                            .setRequired())
                    .addColumn(new ColumnBuilder("Arz", DataType.DOUBLE))
                    .addColumn(new ColumnBuilder("Tul", DataType.DOUBLE))
                    .addColumn(new ColumnBuilder("Tedad", DataType.INT))
                    .addColumn(new ColumnBuilder("Code", DataType.TEXT))
                    .toTable(db);

            Relationship rel3 = new RelationshipBuilder("TypeList", "SizeList")
                    .addColumns("OrderID-FK", "OrderID-FK")
                    .addColumns("TypeID", "TypeID-FK")
                    .setReferentialIntegrity()
                    .setCascadeDeletes()
                    .toRelationship(db);
            printList = new TableBuilder("PrintList")
                    .addColumn(new ColumnBuilder("ID", DataType.LONG).setAutoNumber(true))
                    .addColumn(new ColumnBuilder("Title", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Name", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Date", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Shishe1", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Shishe2", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Spacer", DataType.TEXT))
                    .addColumn(new ColumnBuilder("Gaz", DataType.TEXT))
                    .addColumn(new ColumnBuilder("ARZ", DataType.TEXT))
                    .addColumn(new ColumnBuilder("TUL", DataType.TEXT))
                    .addColumn(new ColumnBuilder("TEDAD", DataType.INT))
                    .addColumn(new ColumnBuilder("CODE", DataType.TEXT))
                    .toTable(db);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "خطا", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void printListMaker(List<GlassBuyInfo> gbis, String title) {
        removeAllPrintList();
        for (GlassBuyInfo gbi : gbis) {
            for (GlassType gt : gbi.getList()) {
                for (Glass g : gt.getList()) {
                    try {
                        printList.addRow(Column.AUTO_NUMBER,
                                "\u200B" + title,
                                gbi.getCustomer(),
                                gbi.getDate().toString(),
                                gt.getType1(),
                                gt.getType2(),
                                gt.getSpacer() + "",
                                gt.getGaz() ? "گاز آرگون" : "بدون گاز",
                                g.getW() + "",
                                g.getH() + "",
                                g.getC(),
                                gbi.getHasCode() ? g.getCode() : "نوع گاز"
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void removeAllPrintList() {
        try {
            for (Row row : printList) {
                printList.deleteRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDataBase(String homeDir) {
        try {

            db = DatabaseBuilder.open(new File(homeDir + "db.mdb"));
            OrderList = db.getTable("OrderList");
            TypeList = db.getTable("TypeList");
            SizeList = db.getTable("SizeList");
            printList = db.getTable("PrintList");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "خطا", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "خطا", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closeDataBase() {
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<GlassBuyInfo> getOrderList() {
        ArrayList<GlassBuyInfo> temp = new ArrayList<>();
        for (Row r : OrderList) {
            temp.add(getOrderInformation(r.getInt("OrderID")));
        }

        return temp;
    }

    public void deleteOrder(int orderID) {
        try {
            Row r = CursorBuilder.findRowByPrimaryKey(OrderList, orderID);
            if (r != null) {
                OrderList.deleteRow(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTypes(int orderID) {
        try {
            for (int i = 1; ; i++) {
                Row row = CursorBuilder.findRowByPrimaryKey(TypeList, orderID, i);
                if (row != null) {
                    TypeList.deleteRow(row);
                } else return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            for (Row row : OrderList) {
                OrderList.deleteRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GlassBuyInfo getOrderInformation(int orderID) {
        try {
            Row row = CursorBuilder.findRowByPrimaryKey(OrderList, orderID);
            if (row != null) {
                Object[] t = row.values().toArray();
                GlassBuyInfo gbi = new GlassBuyInfo((String) t[1], new MyDate((String) t[2], false), (Boolean) t[3]);
                gbi.setList(getTypesInformation(orderID));
                gbi.setID(orderID);
                return gbi;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GlassType> getTypesInformation(int orderID) {
        try {
            ArrayList<GlassType> temp = new ArrayList<>();
            for (int i = 1; ; i++) {
                Row row = CursorBuilder.findRowByPrimaryKey(TypeList, orderID, i);
                if (row != null) {
                    Object[] t = row.values().toArray();
                    temp.add(new GlassType((String) t[2], (String) t[3], ((Short) t[4]).intValue(), (boolean) t[5]));
                    temp.get(temp.size() - 1).setList(getSizesInformation(orderID, i));
                } else
                    return temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Glass> getSizesInformation(int orderID, int typeID) {
        try {
            ArrayList<Glass> temp = new ArrayList<>();
            for (int i = 1; ; i++) {
                Row row = CursorBuilder.findRowByPrimaryKey(SizeList, orderID, typeID, i);
                if (row != null) {
                    Object[] t = row.values().toArray();
                    temp.add(new Glass((double) t[3], (double) t[4], ((Short) t[5]).intValue(), (String) t[6]));
                } else return temp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editDB(int changeID, GlassBuyInfo gbi) {
        try {
            deleteTypes(changeID);
            Row r = CursorBuilder.findRowByPrimaryKey(OrderList, changeID);
            if (r != null) {
                r.put("Name", gbi.getCustomer());
                r.put("Date", gbi.getDate().toString());
                r.put("metrazh", gbi.getMetrazh());
                r.put("tedad", gbi.getTedad());
                r.put("hasCode", gbi.getHasCode());
                OrderList.updateRow(r);
                for (int i = 0; i < gbi.getList().size(); i++) {
                    GlassType gt = gbi.getList().get(i);
                    int TypeID = (int) TypeList.addRow(changeID, i + 1,
                            gbi.getList().get(i).getType1(),
                            gbi.getList().get(i).getType2(),
                            gbi.getList().get(i).getSpacer(),
                            gbi.getList().get(i).getGaz())[1];
                    int SizeID = 1;
                    for (Glass g : gbi.getList().get(i).getList()) {
                        SizeList.addRow(changeID, TypeID, SizeID++, g.getW(), g.getH(), g.getC(), g.getCode());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToDB(GlassBuyInfo gbi) {
        try {
            int orderID = (int) OrderList.addRow(Column.AUTO_NUMBER, gbi.getCustomer(), gbi.getDate().toString(), gbi.getHasCode(), gbi.getMetrazh(), gbi.getTedad())[0];
            for (int i = 0; i < gbi.getList().size(); i++) {
                int TypeID = (int) TypeList.addRow(orderID, i + 1,
                        gbi.getList().get(i).getType1(),
                        gbi.getList().get(i).getType2(),
                        gbi.getList().get(i).getSpacer(),
                        gbi.getList().get(i).getGaz())[1];
                int SizeID = 1;
                for (Glass g : gbi.getList().get(i).getList()) {
                    SizeList.addRow(orderID, TypeID, SizeID++, g.getW(), g.getH(), g.getC(), g.getCode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
