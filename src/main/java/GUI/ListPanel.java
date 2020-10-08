package GUI;

import DS.Glass;
import DS.GlassType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Reza on 02/09/2017.
 */
public class ListPanel extends JPanel {

    private JPanel topPanel, botPanel;
    private JComboBox<String> shishe1, shishe2;
    private JComboBox<Integer> spacer;
    private JCheckBox gaz;
    private ArrayList<AghlamPanel> aghlams;
    private JLabel metrazh, tedad;
    private JButton afzudan, kastan;
    private JTextField aghlam;
    private Font defaultFont;
    private String tempAghlam;
    private int ertefa;
    private DefaultComboBoxModel<String> list1, list2;
    private DefaultComboBoxModel<Integer> list3;
    private Integer numberR;
    // Listeners start
    private ActionListener listButtonPanelListener;
    private FocusListener listButtonPanelFocusListener;
    private FocusListener scrollFocusListener;
    private PropertyChangeListener aghlamListener;
    // Listeners end

    public ListPanel(Font defaultFont, int newHeight, Vector<String> list, int number) {
        super(new BorderLayout(5, 5));
        this.defaultFont = defaultFont;
        ertefa = 40;
        numberR = number;
        list1 = new DefaultComboBoxModel<>();
        list2 = new DefaultComboBoxModel<>();
        Integer[] tempSpacer = {6, 8, 10, 12, 14, 16};
        list3 = new DefaultComboBoxModel<>(tempSpacer);
        aghlamListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("metrazh")) {
                    syncMetrazh();
                } else if (e.getPropertyName().equals("tedad")) {
                    syncTedad();
                } else if (e.getPropertyName().equals("focus")) {
                    Component focusedComponent = aghlams.get((Integer) e.getOldValue() - 1);
                    botPanel.scrollRectToVisible(focusedComponent.getBounds(null));
                    repaint();
                }
            }
        };

        listButtonPanelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(afzudan)) {
                    if (Integer.parseInt(aghlam.getText()) != 99) {
                        aghlam.setText(Integer.parseInt(aghlam.getText()) + 1 + "");
                        syncAghlamPanels();
                    }
                } else if (e.getSource().equals(kastan)) {
                    if (Integer.parseInt(aghlam.getText()) != 1) {
                        aghlam.setText(Integer.parseInt(aghlam.getText()) - 1 + "");
                        syncAghlamPanels();
                    }
                }
            }
        };

        listButtonPanelFocusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource().equals(aghlam))
                    tempAghlam = aghlam.getText();

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource().equals(aghlam))
                    if (!aghlam.getText().matches("[\\d]+"))
                        aghlam.setText(tempAghlam);
                    else if (Integer.parseInt(aghlam.getText()) < 1 || Integer.parseInt(aghlam.getText()) > 99)
                        aghlam.setText(tempAghlam);
                    else if (!aghlam.getText().equals(tempAghlam)) {
                        syncAghlamPanels();
                    }

            }
        };
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        scrollFocusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                firePropertyChange("focus", numberR, null);
            }
        };
        topPanel = new JPanel(new FlowLayout());
        topPanel.setPreferredSize(new Dimension(250, 180));
        botPanel = new JPanel(new FlowLayout());
        botPanel.setPreferredSize(new Dimension(250, 35));

        shishe1 = new JComboBox<>();
        shishe1.setFont(this.defaultFont);
        shishe1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        ((JLabel) shishe1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        shishe1.setModel(this.list1);
        shishe1.setPreferredSize(new Dimension(245, 30));
        shishe1.setToolTipText("<html><font size=\"6\" face=\"tahoma\">نوع شیشه اول</font> </html>");
        shishe1.addFocusListener(scrollFocusListener);
        topPanel.add(shishe1);


        shishe2 = new JComboBox<>();
        shishe2.setFont(this.defaultFont);
        shishe2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        ((JLabel) shishe2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        shishe2.setPreferredSize(new Dimension(245, 30));
        shishe2.setModel(this.list2);
        shishe2.setToolTipText("<html><font size=\"6\" face=\"tahoma\">نوع شیشه دوم</font> </html>");
        shishe2.addFocusListener(scrollFocusListener);
        topPanel.add(shishe2);

        syncShisheLiast(list);

        spacer = new JComboBox<>();
        spacer.setFont(this.defaultFont);
        spacer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        ((JLabel) spacer.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        spacer.setPreferredSize(new Dimension(70, 30));
        spacer.setModel(list3);
        list3.setSelectedItem(12);
        spacer.setToolTipText("<html><font size=\"6\" face=\"tahoma\">اسپیسر</font> </html>");
        spacer.addFocusListener(scrollFocusListener);

        topPanel.add(spacer);


        gaz = new JCheckBox("گاز آرگون", true);
        gaz.setFont(this.defaultFont);
        gaz.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        gaz.setPreferredSize(new Dimension(170, 30));
        gaz.setToolTipText("<html><font size=\"6\" face=\"tahoma\">گاز</font> </html>");
        gaz.addFocusListener(scrollFocusListener);
        gaz.addFocusListener(scrollFocusListener);

        topPanel.add(gaz);

        metrazh = new JLabel("متراژ:1.0");
        metrazh.setFont(this.defaultFont);
        metrazh.setPreferredSize(new Dimension(130, 30));
        metrazh.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        topPanel.add(metrazh);

        tedad = new JLabel("تعداد:1");
        tedad.setFont(this.defaultFont);
        tedad.setPreferredSize(new Dimension(110, 30));
        tedad.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        topPanel.add(tedad);


        kastan = new JButton("-");
        kastan.setFont(this.defaultFont);
        kastan.setPreferredSize(new Dimension(55, 30));
        kastan.addActionListener(listButtonPanelListener);
        kastan.setFocusable(false);
        topPanel.add(kastan);

        aghlam = new JTextField("1");
        aghlam.setFont(this.defaultFont);
        aghlam.setPreferredSize(new Dimension(122, 30));
        aghlam.setHorizontalAlignment(SwingConstants.CENTER);
        aghlam.addFocusListener(listButtonPanelFocusListener);
        aghlam.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                aghlam.selectAll();
            }
        });
        topPanel.add(aghlam);

        afzudan = new JButton("+");
        afzudan.setFont(this.defaultFont);
        afzudan.setPreferredSize(new Dimension(55, 30));
        afzudan.addActionListener(listButtonPanelListener);
        afzudan.setFocusable(false);

        topPanel.add(afzudan);
        Set<AWTKeyStroke> defaultKeys = topPanel.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newKeys = new HashSet<>(defaultKeys);
        newKeys.add(KeyStroke.getKeyStroke("pressed ENTER"));
        topPanel.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);

        aghlams = new ArrayList<>();
        aghlams.add(new AghlamPanel(1, this.defaultFont));
        aghlams.get(aghlams.size() - 1).addPropertyChangeListener(aghlamListener);
        botPanel.add(aghlams.get(aghlams.size() - 1));
        JScrollPane tablePanelPane = new JScrollPane(botPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tablePanelPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        tablePanelPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        tablePanelPane.setPreferredSize(new Dimension(250, 720));
        tablePanelPane.getVerticalScrollBar().setUnitIncrement(30);
        setPreferredSize(new Dimension(250, newHeight));
        add(topPanel, BorderLayout.NORTH);
        add(tablePanelPane, BorderLayout.CENTER);
    }

    private void syncTedad() {
        firePropertyChange("tedad", null, null);
        tedad.setText("تعداد:" + getTedad());
    }

    private void syncMetrazh() {
        firePropertyChange("metrazh", null, null);
        metrazh.setText("متراژ:" + String.format("%.2f", getMetrazh() / 10000));
    }

    public int getTedad() {
        int t = 0;
        for (AghlamPanel lp : aghlams) {
            t += lp.getTedad();
        }
        return t;
    }

    public double getMetrazh() {
        double m = 0;
        for (AghlamPanel lp : aghlams)
            m += lp.getMetrazh();
        return m;
    }

    public void syncShisheLiast(Vector<String> newList) {
        if (newList.indexOf(list1.getSelectedItem()) == -1) {
            list1 = new DefaultComboBoxModel<>(newList);
            list1.setSelectedItem("شیشه را انتخاب کنید");
        } else {
            String temp = (String) list1.getSelectedItem();
            list1 = new DefaultComboBoxModel<>(newList);
            list1.setSelectedItem(temp);
        }

        if (newList.indexOf(list2.getSelectedItem()) == -1) {
            list2 = new DefaultComboBoxModel<>(newList);
            list2.setSelectedItem("شیشه را انتخاب کنید");
        } else {
            String temp = (String) list2.getSelectedItem();
            list2 = new DefaultComboBoxModel<>(newList);
            list2.setSelectedItem(temp);
        }
        shishe1.setModel(this.list1);
        shishe2.setModel(this.list2);

    }

    public boolean checkInformations() {
        boolean flag=true;
        if (list1.getSelectedItem().equals("شیشه را انتخاب کنید")) {
            shishe1.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            flag= false;
        } else
            shishe1.setBorder(new JComboBox<String>().getBorder());

        if (list2.getSelectedItem().equals("شیشه را انتخاب کنید")) {
            shishe2.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            flag= false;
        } else
            shishe2.setBorder(new JComboBox<String>().getBorder());
        for(AghlamPanel ap:aghlams)
            if(!ap.checkInformation())
                flag=false;
        return flag;
    }

    public String[] getInformations() {
        String[] temp = {list1.getSelectedItem().toString(), list2.getSelectedItem().toString(), list3.getSelectedItem().toString(), gaz.isSelected() ? "آرگون" : "بدون گاز", aghlam.getText()};
        return temp;
    }

    public ArrayList<Double[]> getAghlams() {
        ArrayList<Double[]> temp = new ArrayList<>();
        for (AghlamPanel temp1 : aghlams) {
            Double[] temp2 = new Double[3];
            temp2[0] = temp1.getArz();
            temp2[1] = temp1.getTul();
            temp2[2] = (double) temp1.getTedad();
            temp.add(temp2);
        }
        return temp;
    }

    private void syncAghlamPanels() {
        int newN = Integer.parseInt(aghlam.getText());
        int oldN = aghlams.size();
        for (; newN < oldN; newN++) {
            firePropertyChange("metrazh", null, null);
            firePropertyChange("tedad", null, null);
            syncMetrazh();
            syncTedad();
            botPanel.remove(aghlams.get(aghlams.size() - 1));
            aghlams.remove(aghlams.size() - 1);
            botPanel.setPreferredSize(new Dimension(250, ertefa = ertefa - 40));
            revalidate();
            repaint();
        }
        for (; newN > oldN; oldN++) {
            aghlams.add(new AghlamPanel(aghlams.size() + 1, defaultFont));
            aghlams.get(aghlams.size() - 1).addPropertyChangeListener(aghlamListener);
            botPanel.add(aghlams.get(aghlams.size() - 1));
            firePropertyChange("metrazh", null, null);
            firePropertyChange("tedad", null, null);
            syncMetrazh();
            syncTedad();
            botPanel.setPreferredSize(new Dimension(250, ertefa = ertefa + 40));
            revalidate();
            repaint();
        }
    }


    public void setNewPanel() {
        list1.setSelectedItem("شیشه را انتخاب کنید");
        list2.setSelectedItem("شیشه را انتخاب کنید");
        list3.setSelectedItem(12);
        gaz.setSelected(true);
        aghlam.setText("1");
        syncAghlamPanels();
        aghlams.get(0).setNewPanel();
    }

    public GlassType getGlassType() {
        if (checkInformations()) {
            GlassType gt = new GlassType((String) list1.getSelectedItem(),
                    (String) list2.getSelectedItem(),
                    (Integer) list3.getSelectedItem(),
                    gaz.isSelected());
            for (AghlamPanel ap : aghlams) {
                Glass g = ap.getGlass();
                if (g == null)
                    return null;
                gt.getList().add(g);
            }
            return gt;
        }
        return null;
    }

    public void setAttribute(GlassType gt) {
        list1.setSelectedItem(gt.getType1());
        list2.setSelectedItem(gt.getType2());
        list3.setSelectedItem(gt.getSpacer());
        gaz.setSelected(gt.getGaz());
        aghlam.setText(gt.getList().size() + "");
        syncAghlamPanels();
        for (int i = 0; i < gt.getList().size(); i++) {
            aghlams.get(i).setAttribute(gt.getList().get(i));
        }
        syncMetrazh();
        syncTedad();
    }

}
