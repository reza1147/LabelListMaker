package GUI;

import DS.Glass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Reza on 03/09/2017.
 */
public class AghlamPanel extends JPanel {
    private JLabel zarb, khat, radif;
    private JTextField arz, tul, tedad, code;
    private Font defaultFont;
    private FocusListener aghlamPanelListener;
    private Integer numberR;
    private Double[][] noneStandarList;
    private Boolean hasCode;

    public AghlamPanel(int number, Font defaultFont, Double[][] noneStandarList, Boolean hasCode) {
        super(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        setPreferredSize(new Dimension(300, 39));
        this.hasCode = hasCode;
        this.defaultFont = defaultFont;
        this.numberR = number;
        this.noneStandarList = noneStandarList;
        Set<AWTKeyStroke> defaultKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newKeys = new HashSet<>(defaultKeys);
        newKeys.add(KeyStroke.getKeyStroke("pressed ENTER"));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);
        aghlamPanelListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField) e.getSource()).selectAll();
                firePropertyChange("focus", numberR, null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                checkInformation();
                firePropertyChange("tedad", null, null);
                firePropertyChange("metrazh", null, null);
            }
        };

        zarb = new JLabel("X");
        zarb.setFont(this.defaultFont);

        khat = new JLabel("_");
        khat.setFont(this.defaultFont);

        radif = new JLabel(number + ":");
        radif.setFont(this.defaultFont);

        arz = new JTextField("100");
        arz.setPreferredSize(new Dimension(58, 30));
        arz.setFont(this.defaultFont);
        arz.addFocusListener(aghlamPanelListener);

        tul = new JTextField("100");
        tul.setPreferredSize(new Dimension(58, 30));
        tul.setFont(this.defaultFont);
        tul.addFocusListener(aghlamPanelListener);

        tedad = new JTextField("1");
        tedad.setPreferredSize(new Dimension(45, 30));
        tedad.setFont(this.defaultFont);
        tedad.addFocusListener(aghlamPanelListener);
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        code = new JTextField("");
        code.setPreferredSize(new Dimension(55, 30));
        code.setFont(this.defaultFont);
        code.addFocusListener(aghlamPanelListener);
        code.setVisible(hasCode);
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        add(radif);
        add(arz);
        add(zarb);
        add(tul);
        add(khat);
        add(tedad);
        add(code);

        syncToolTip();
    }

    public boolean checkInformation() {
        boolean flag = true;
        if (arz.getText().matches("([0-9]+[.][0-9])|([0-9]+)")) {
            arz.setBorder(new JTextField().getBorder());
        } else {
            flag = false;
            arz.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        if (tul.getText().matches("([0-9]+[.][0-9])|([0-9]+)")) {
            tul.setBorder(new JTextField().getBorder());
        } else {
            flag = false;
            tul.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        if (tedad.getText().matches("[0-9]+")) {
            tedad.setBorder(new JTextField().getBorder());
        } else {
            flag = false;
            tedad.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        if (flag) {
            checkNoneStandard();
            syncToolTip();
        }
        return flag;
    }

    private void checkNoneStandard() {
        int flag = 1;
        if (noneStandarList[0] == null) {
            flag *= 1;
        } else if (noneStandarList[0].length == 1) {
            if (getSingleMetrazh() / 10000 < noneStandarList[0][0])
                flag *= 2;
        } else if (noneStandarList[0].length == 2) {
            if (getArz() < noneStandarList[0][0] || getTul() < noneStandarList[0][1])
                flag *= 2;
        }
        if (noneStandarList[1] == null) {
            flag *= 1;
        } else if (noneStandarList[1].length == 1) {
            if (getSingleMetrazh() / 10000 > noneStandarList[1][0])
                flag *= 3;
        } else if (noneStandarList[1].length == 2) {
            if (getArz() > noneStandarList[1][0] || getTul() > noneStandarList[1][1])
                flag *= 3;
        }
        switch (flag) {
            case 1:
                setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                break;
            case 2:
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                break;
            case 3:
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                break;
            case 6:
                setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
                break;
        }
    }

    public double getMetrazh() {
        try {
            return Double.parseDouble(arz.getText()) * Double.parseDouble(tul.getText()) * Integer.parseInt(tedad.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public double getRizMetrazh() {
        if (noneStandarList[0] == null) {
            return 0;
        } else if (noneStandarList[0].length == 1) {
            if (getSingleMetrazh() / 10000 < noneStandarList[0][0])
                return getMetrazh();
        } else if (noneStandarList[0].length == 2) {
            if (getArz() < noneStandarList[0][0] || getTul() < noneStandarList[0][1])
                return getMetrazh();
        }
        return 0;

    }

    public double getBigMetrazh() {
        if (noneStandarList[1] == null) {
            return 0;
        } else if (noneStandarList[1].length == 1) {
            if (getSingleMetrazh() / 10000 > noneStandarList[1][0])
                return getMetrazh();
        } else if (noneStandarList[1].length == 2) {
            if (getArz() > noneStandarList[1][0] || getTul() > noneStandarList[1][1])
                return getMetrazh();
        }
        return 0;
    }

    public double getSingleMetrazh() {
        try {
            return Double.parseDouble(arz.getText()) * Double.parseDouble(tul.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getTedad() {
        try {
            return Integer.parseInt(tedad.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public double getTul() {
        return Math.max(Double.parseDouble(tul.getText()), Double.parseDouble(arz.getText()));
    }

    public double getArz() {
        return Math.min(Double.parseDouble(tul.getText()), Double.parseDouble(arz.getText()));
    }

    public void setAttribute(Glass g) {
        arz.setText(g.getW() + "");
        tul.setText(g.getH() + "");
        tedad.setText(g.getC() + "");
        code.setText(g.getCode());
    }

    public void setHasCode(Boolean hasCode) {
        this.hasCode = hasCode;
        code.setVisible(hasCode);
        revalidate();
        repaint();
    }

    public void setNewPanel() {
        arz.setText("100");
        tul.setText("100");
        tedad.setText("1");
        code.setText("");
        firePropertyChange("tedad", null, null);
        firePropertyChange("metrazh", null, null);
    }

    public Glass getGlass() {
        if (checkInformation()) {
            Double a = getArz();
            Double t = getTul();
            Integer te = Integer.parseInt(tedad.getText());
            if (a > 0 && t > 0 && te > 0)
                return new Glass(a, t, te, code.getText());
        }
        return null;
    }

    public void syncNoneStandard(Double[][] noneStandarList) {
        this.noneStandarList = noneStandarList;
        checkNoneStandard();
    }

    private void syncToolTip() {
        String color = "black";
        if (getRizMetrazh() > 0)
            color = "green";
        else if (getBigMetrazh() > 0)
            color = "blue";
        String tip = String.format("<html><font size=\"6\" face=\"tahoma\" color=\"%s\">%f</font> </html>", color, getMetrazh());
        zarb.setToolTipText(tip);
        khat.setToolTipText(tip);
        radif.setToolTipText(tip);
        arz.setToolTipText(tip);
        tul.setToolTipText(tip);
        tedad.setToolTipText(tip);
    }
}
