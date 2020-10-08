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

    public AghlamPanel(int number, Font defaultFont) {
        super(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        setPreferredSize(new Dimension(250, 35));
        this.defaultFont = defaultFont;
        this.numberR = number;
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
        return flag;
    }

    public double getMetrazh() {
        try {
            return Double.parseDouble(arz.getText()) * Double.parseDouble(tul.getText()) * Integer.parseInt(tedad.getText());
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
        return Double.parseDouble(tul.getText());
    }

    public double getArz() {
        return Double.parseDouble(arz.getText());
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
            Double a = Double.parseDouble(arz.getText());
            Double t = Double.parseDouble(tul.getText());
            Integer te = Integer.parseInt(tedad.getText());
            if (a > 0 && t > 0 && te > 0)
                return new Glass(a, t, te);
        }
        return null;
    }
}
