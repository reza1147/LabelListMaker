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
    private JTextField arz, tul, tedad;
    private Font defaultFont;
    private FocusListener aghlamPanelListener;
    private Integer numberR;
    private Double[][] noneStandarList;

    public AghlamPanel(int number, Font defaultFont, Double[][] noneStandarList) {
        super(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        setPreferredSize(new Dimension(250, 39));
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

        add(radif);
        add(arz);
        add(zarb);
        add(tul);
        add(khat);
        add(tedad);

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
        if (flag)
            checkNoneStandard();
        return flag;
    }

    private void checkNoneStandard() {
        if (noneStandarList[0] == null) {
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        } else if (noneStandarList[0].length == 1) {
            if (getSingleMetrazh() / 10000 < noneStandarList[0][0])
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        } else if (noneStandarList[0].length == 2) {
            if (getArz() < noneStandarList[0][0] || getTul() < noneStandarList[0][1])
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        }
        if (noneStandarList[1] == null) {
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        } else if (noneStandarList[1].length == 1) {
            if (getSingleMetrazh() / 10000 > noneStandarList[1][0])
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        } else if (noneStandarList[1].length == 2) {
            if (getArz() > noneStandarList[1][0] || getTul() > noneStandarList[1][1])
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
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
    }

    public void setNewPanel() {
        arz.setText("100");
        tul.setText("100");
        tedad.setText("1");
        firePropertyChange("tedad", null, null);
        firePropertyChange("metrazh", null, null);
    }

    public Glass getGlass() {
        if (checkInformation()) {
            Double a = getArz();
            Double t = getTul();
            Integer te = Integer.parseInt(tedad.getText());
            if (a > 0 && t > 0 && te > 0)
                return new Glass(a, t, te);
        }
        return null;
    }

    public void syncNoneStandard(Double[][] noneStandarList) {
        this.noneStandarList = noneStandarList;
        checkNoneStandard();
    }
}
