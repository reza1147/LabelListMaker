package GUI;

import org.jdom2.Element;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Vector;

/**
 * Created by rezat on 12/09/2017.
 */
public class Setting extends JFrame {

    private final MainFrame parent;
    private final Setting me;
    private boolean anyThingChange, saveChangesB;
    private final JButton saveChanges;
    private final Font defaultFont;

    // listTab Components start
    private JPanel listPanel;
    private JButton up, down, delete, add, edit;
    private JList<String> shisheList;
    private DefaultListModel<String> list;
    // listTab Components end

    // AboutTab Components start
    private JPanel aboutPanel;
    // AboutTab Components end


    // NonStandardTab Components start
    private JPanel noneStandardPanel;
    private JTextField metrazhTxt1, arzTxt1, tulTxt1, metrazhTxt2, arzTxt2, tulTxt2;
    private JRadioButton none1, metrazh1, abaad1, none2, metrazh2, abaad2;
    // NonStandardTab Components end

    public Setting(Component parent) throws HeadlessException {
        super("تنظیمات");
        this.parent = (MainFrame) parent;
        this.me = this;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(600, 600);
        anyThingChange = false;
        saveChangesB = false;
        defaultFont = new Font("tahoma", Font.PLAIN, 20);
        setMinimumSize(new Dimension(600, 600));
        setResizable(false);
        setLocationRelativeTo(parent);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(5, 5));
        getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, this.getBackground()));
        JTabbedPane tempTab = new JTabbedPane(SwingConstants.TOP);
        JPanel tempPanel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        URL iconURL = getClass().getResource("/setting.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNoneStandardForm()) {
                    JOptionPane.showMessageDialog(me, "ابتدا خطا ها را اصلاح کنید!!", "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (e.getSource().equals(saveChanges)) {
                        saveChangesB = true;
                    }
                    dispose();
                }
            }
        };

        saveChanges = new JButton("ذخیره تغییرات");
        saveChanges.setFont(defaultFont);
        saveChanges.setPreferredSize(new Dimension(185, 50));
        saveChanges.addActionListener(buttonListener);
        tempPanel1.add(saveChanges);

        JButton cansel = new JButton("لغو");
        cansel.setFont(defaultFont);
        cansel.setPreferredSize(new Dimension(185, 50));
        cansel.addActionListener(buttonListener);
        tempPanel1.add(cansel);


        initListPanel();
        initNoneStandardPanel();
        initAboutPanel();

        tempTab.add("لیست شیشه ها", listPanel);
        tempTab.add("خارج استاندارد", noneStandardPanel);
        tempTab.add("درباره نرم افزار", aboutPanel);
        tempTab.setFont(defaultFont);


        add(tempPanel1, BorderLayout.SOUTH);
        add(tempTab, BorderLayout.CENTER);


        addWindowListener(new WindowAdapter() {

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                parent.requestFocus();
            }


            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                parent.requestFocus();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                if (anyThingChange) {
                    Vector<String> newShisheList = new Vector<>();
                    for (int i = 0; i < list.getSize(); i++)
                        newShisheList.addElement(list.get(i));
                    if (saveChangesB)
                        firePropertyChange("listShishe", ((MainFrame) parent).getListShishe(), newShisheList);
                    else {
                        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        panel.setPreferredSize(new Dimension(350, 35));

                        JLabel label = new JLabel("تغییرات را ذخیره می کنید؟");
                        label.setFont(defaultFont);
                        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                        label.setPreferredSize(new Dimension(350, 35));
                        panel.add(label);

                        String[] options = {"Yes", "No"};
                        JOptionPane myPane = new JOptionPane();
                        myPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                        myPane.setMessage(panel);
                        myPane.setOptions(options);
                        myPane.setInitialValue("Yes");
                        myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        JDialog myDialog = myPane.createDialog(parent, "ذخیره تغییرات");
                        myDialog.setVisible(true);
                        Object answer = myPane.getValue();
                        if (answer.equals("Yes"))
                            firePropertyChange("listShishe", ((MainFrame) parent).getListShishe(), newShisheList);
                    }
                }
                if (saveChangesB)
                    firePropertyChange("NoneStandard",
                            null,
                            getNoneStandard());
                parent.setEnabled(true);
                parent.requestFocus();

            }
        });
        setVisible(true);

    }

    // infoPanel Components initializer
    @SuppressWarnings("unchecked")
    private void initListPanel() {
        listPanel = new JPanel(new BorderLayout(5, 5));
        listPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel tempPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        tempPanel1.setPreferredSize(new Dimension(50, 330));

        ListSelectionListener listSelectionListener = e -> {
            if (e.getSource() instanceof JList) {
                JList<String> tempList = (JList<String>) e.getSource();
                if (tempList.getSelectedIndex() != -1) {
                    edit.setEnabled(true);
                    delete.setEnabled(true);
                    up.setEnabled(tempList.getSelectedIndex() != 0);
                    down.setEnabled(tempList.getSelectedIndex() != tempList.getLastVisibleIndex());
                } else {
                    edit.setEnabled(false);
                    delete.setEnabled(false);
                    up.setEnabled(false);
                    down.setEnabled(false);
                }
            }
        };

        ActionListener buttonPanelListener = e -> {
            anyThingChange = true;
            if (e.getSource().equals(up)) {
                int indexOfSelected = shisheList.getSelectedIndex();
                String tmp = list.get(indexOfSelected);
                list.setElementAt(list.get(indexOfSelected - 1), indexOfSelected);
                list.setElementAt(tmp, indexOfSelected - 1);
                indexOfSelected = indexOfSelected - 1;
                shisheList.setSelectedIndex(indexOfSelected);
            } else if (e.getSource().equals(down)) {
                int indexOfSelected = shisheList.getSelectedIndex();
                String tmp = list.get(indexOfSelected);
                list.setElementAt(list.get(indexOfSelected + 1), indexOfSelected);
                list.setElementAt(tmp, indexOfSelected + 1);
                indexOfSelected = indexOfSelected + 1;
                shisheList.setSelectedIndex(indexOfSelected);
            } else if (e.getSource().equals(add)) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                panel.setPreferredSize(new Dimension(300, 80));

                JLabel label = new JLabel("نام شیشه جدید را وارد کنید:");
                label.setFont(defaultFont);
                label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                label.setPreferredSize(new Dimension(290, 35));

                JTextField temp = new JTextField("");
                temp.setFont(defaultFont);
                temp.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                temp.setAlignmentX(Component.RIGHT_ALIGNMENT);
                temp.setPreferredSize(new Dimension(290, 35));

                panel.add(label);
                panel.add(temp);
                String[] options = {"Yes", "No"};
                JOptionPane myPane = new JOptionPane();
                myPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                myPane.setMessage(panel);
                myPane.setOptions(options);
                myPane.setInitialValue("Yes");
                myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                JDialog myDialog = myPane.createDialog(((JButton) e.getSource()).getParent(), "اضافه کردن شیشه");
                myDialog.setVisible(true);
                Object answer = myPane.getValue();
                if (answer != null)
                    if (answer.equals("Yes"))
                        list.addElement(temp.getText());
            } else if (e.getSource().equals(delete)) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                panel.setPreferredSize(new Dimension(350, 35));

                JLabel label = new JLabel("از حذف کردن این مورد مطمئن هستید؟");
                label.setFont(defaultFont);
                label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                label.setPreferredSize(new Dimension(350, 35));

                panel.add(label);
                String[] options = {"Yes", "No"};
                JOptionPane myPane = new JOptionPane();
                myPane.setMessageType(JOptionPane.ERROR_MESSAGE);
                myPane.setMessage(panel);
                myPane.setOptions(options);
                myPane.setInitialValue("Yes");
                myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                JDialog myDialog = myPane.createDialog(((JButton) e.getSource()).getParent(), "حذف کردن شیشه");
                myDialog.setVisible(true);
                Object answer = myPane.getValue();
                if (answer.equals("Yes"))
                    list.remove(shisheList.getSelectedIndex());
            } else if (e.getSource().equals(edit)) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                panel.setPreferredSize(new Dimension(300, 80));

                JLabel label = new JLabel("نام شیشه جدید را وارد کنید:");
                label.setFont(defaultFont);
                label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                label.setPreferredSize(new Dimension(290, 35));

                JTextField temp = new JTextField(list.get(shisheList.getSelectedIndex()));
                temp.setFont(defaultFont);
                temp.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                temp.setAlignmentX(Component.RIGHT_ALIGNMENT);
                temp.setPreferredSize(new Dimension(290, 35));

                panel.add(label);
                panel.add(temp);
                String[] options = {"Yes", "No"};
                JOptionPane myPane = new JOptionPane();
                myPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                myPane.setMessage(panel);
                myPane.setOptions(options);
                myPane.setInitialValue("Yes");
                myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                JDialog myDialog = myPane.createDialog(((JButton) e.getSource()).getParent(), "تغییر در نام شیشه");
                myDialog.setVisible(true);
                Object answer = myPane.getValue();
                if (answer.equals("Yes"))
                    list.setElementAt(temp.getText(), shisheList.getSelectedIndex());
            }
        };


        up = new JButton("↑");
        up.setFont(defaultFont);
        up.setPreferredSize(new Dimension(60, 60));
        up.addActionListener(buttonPanelListener);
        up.setEnabled(false);
        tempPanel1.add(up);

        down = new JButton("↓");
        down.setFont(defaultFont);
        down.setPreferredSize(new Dimension(60, 60));
        down.addActionListener(buttonPanelListener);
        down.setEnabled(false);
        tempPanel1.add(down);

        list = new DefaultListModel<>();

        shisheList = new JList<>(list);
        shisheList.setFont(defaultFont);
        shisheList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) shisheList.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        shisheList.setBorder(new LineBorder(Color.black, 1));
        shisheList.addListSelectionListener(listSelectionListener);
        shisheList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (String temp : parent.getListShishe()) {
            list.addElement(temp);
        }
        JScrollPane tempBarPanel = new JScrollPane(shisheList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        JPanel tempPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        tempPanel2.setPreferredSize(new Dimension(300, 50));

        edit = new JButton("ویرایش");
        edit.setFont(defaultFont);
        edit.setPreferredSize(new Dimension(100, 45));
        edit.setEnabled(false);
        edit.addActionListener(buttonPanelListener);
        tempPanel2.add(edit);

        delete = new JButton("حذف");
        delete.setFont(defaultFont);
        delete.setPreferredSize(new Dimension(100, 45));
        delete.setEnabled(false);
        delete.addActionListener(buttonPanelListener);
        tempPanel2.add(delete);

        add = new JButton("افزودن");
        add.setFont(defaultFont);
        add.setPreferredSize(new Dimension(100, 45));
        add.addActionListener(buttonPanelListener);
        tempPanel2.add(add);

        listPanel.add(tempPanel2, BorderLayout.NORTH);
        listPanel.add(tempBarPanel, BorderLayout.CENTER);
        listPanel.add(tempPanel1, BorderLayout.WEST);
    }

    private void initNoneStandardPanel() {
        noneStandardPanel = new JPanel(new GridBagLayout());

        JPanel lessThanStandard = new JPanel(new GridBagLayout());
        lessThanStandard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(5, 5, 5, 5),
                        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false),
                                "خارج استاندارد ریز بودن",
                                TitledBorder.LEADING,
                                TitledBorder.DEFAULT_POSITION,
                                defaultFont,
                                Color.BLACK)
                ));
        lessThanStandard.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        lessThanStandard.setAlignmentX(Component.LEFT_ALIGNMENT);


        none1 = new JRadioButton("خاموش");
        none1.setFont(defaultFont);
        none1.setSelected(true);
        none1.setActionCommand("none1");


        metrazh1 = new JRadioButton("بر اساس متراژ");
        metrazh1.setFont(defaultFont);
        metrazh1.setActionCommand("metrazh2");

        abaad1 = new JRadioButton("بر اساس ابعاد");
        abaad1.setFont(defaultFont);
        abaad1.setActionCommand("abaad2");

        ButtonGroup bg = new ButtonGroup();
        bg.add(none1);
        bg.add(metrazh1);
        bg.add(abaad1);


        metrazhTxt1 = new JTextField();
        metrazhTxt1.setFont(defaultFont);
        metrazhTxt1.setPreferredSize(new Dimension(100, 30));
        metrazhTxt1.setUI(new HintTextFieldUI("متراژ", true, Color.GRAY));
        parent.addIsListener(metrazhTxt1, Double.MIN_VALUE);
        metrazhTxt1.setEnabled(false);

        arzTxt1 = new JTextField();
        arzTxt1.setFont(defaultFont);
        arzTxt1.setPreferredSize(new Dimension(100, 30));
        arzTxt1.setUI(new HintTextFieldUI("عرض", true, Color.GRAY));
        parent.addIsListener(arzTxt1, Double.MIN_VALUE);
        arzTxt1.setEnabled(false);

        tulTxt1 = new JTextField();
        tulTxt1.setFont(defaultFont);
        tulTxt1.setPreferredSize(new Dimension(100, 30));
        tulTxt1.setUI(new HintTextFieldUI("طول", true, Color.GRAY));
        parent.addIsListener(tulTxt1, Double.MIN_VALUE);
        tulTxt1.setEnabled(false);


        ActionListener actionListener = e -> {
            switch (e.getActionCommand()) {
                case "none1":
                    metrazhTxt1.setEnabled(false);
                    arzTxt1.setEnabled(false);
                    tulTxt1.setEnabled(false);
                    break;
                case "metrazh2":
                    metrazhTxt1.setEnabled(true);
                    arzTxt1.setEnabled(false);
                    tulTxt1.setEnabled(false);
                    break;
                case "abaad2":
                    arzTxt1.setEnabled(true);
                    tulTxt1.setEnabled(true);
                    metrazhTxt1.setEnabled(false);
                    break;
            }
        };

        none1.addActionListener(actionListener);
        metrazh1.addActionListener(actionListener);
        abaad1.addActionListener(actionListener);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        lessThanStandard.add(none1, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        lessThanStandard.add(metrazh1, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        lessThanStandard.add(metrazhTxt1, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        lessThanStandard.add(abaad1, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        lessThanStandard.add(arzTxt1, gc);

        gc.gridx = 2;
        gc.gridy = 2;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        lessThanStandard.add(tulTxt1, gc);


        JPanel moreThanStandard = new JPanel(new GridBagLayout());
        moreThanStandard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(5, 5, 5, 5),
                        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false),
                                "خارج استاندارد بزرگ بودن",
                                TitledBorder.LEADING,
                                TitledBorder.DEFAULT_POSITION,
                                defaultFont,
                                Color.BLACK)
                ));
        moreThanStandard.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        moreThanStandard.setAlignmentX(Component.LEFT_ALIGNMENT);


        none2 = new JRadioButton("خاموش");
        none2.setFont(defaultFont);
        none2.setSelected(true);
        none2.setActionCommand("none2");


        metrazh2 = new JRadioButton("بر اساس متراژ");
        metrazh2.setFont(defaultFont);
        metrazh2.setActionCommand("metrazh2");

        abaad2 = new JRadioButton("بر اساس ابعاد");
        abaad2.setFont(defaultFont);
        abaad2.setActionCommand("abaad2");

        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(none2);
        bg2.add(metrazh2);
        bg2.add(abaad2);


        metrazhTxt2 = new JTextField();
        metrazhTxt2.setFont(defaultFont);
        metrazhTxt2.setPreferredSize(new Dimension(100, 30));
        metrazhTxt2.setUI(new HintTextFieldUI("متراژ", true, Color.GRAY));
        parent.addIsListener(metrazhTxt2, Double.MIN_VALUE);
        metrazhTxt2.setEnabled(false);

        arzTxt2 = new JTextField();
        arzTxt2.setFont(defaultFont);
        arzTxt2.setPreferredSize(new Dimension(100, 30));
        arzTxt2.setUI(new HintTextFieldUI("عرض", true, Color.GRAY));
        parent.addIsListener(arzTxt2, Double.MIN_VALUE);
        arzTxt2.setEnabled(false);

        tulTxt2 = new JTextField();
        tulTxt2.setFont(defaultFont);
        tulTxt2.setPreferredSize(new Dimension(100, 30));
        tulTxt2.setUI(new HintTextFieldUI("طول", true, Color.GRAY));
        parent.addIsListener(tulTxt2, Double.MIN_VALUE);
        tulTxt2.setEnabled(false);


        ActionListener actionListener2 = e -> {
            switch (e.getActionCommand()) {
                case "none1":
                    metrazhTxt2.setEnabled(false);
                    arzTxt2.setEnabled(false);
                    tulTxt2.setEnabled(false);
                    break;
                case "metrazh2":
                    metrazhTxt2.setEnabled(true);
                    arzTxt2.setEnabled(false);
                    tulTxt2.setEnabled(false);
                    break;
                case "abaad2":
                    arzTxt2.setEnabled(true);
                    tulTxt2.setEnabled(true);
                    metrazhTxt2.setEnabled(false);
                    break;
            }
        };

        none2.addActionListener(actionListener2);
        metrazh2.addActionListener(actionListener2);
        abaad2.addActionListener(actionListener2);

        GridBagConstraints gc2 = new GridBagConstraints();
        gc2.gridx = 0;
        gc2.gridy = 0;
        gc2.weightx = 1;
        gc2.weighty = 1;
        gc2.fill = GridBagConstraints.NONE;
        moreThanStandard.add(none2, gc2);

        gc2.gridx = 0;
        gc2.gridy = 1;
        gc2.weightx = 1;
        gc2.weighty = 1;
        gc2.fill = GridBagConstraints.NONE;
        moreThanStandard.add(metrazh2, gc2);

        gc2.gridx = 1;
        gc2.gridy = 1;
        gc2.weightx = 1;
        gc2.weighty = 1;
        gc2.fill = GridBagConstraints.NONE;
        moreThanStandard.add(metrazhTxt2, gc2);

        gc2.gridx = 0;
        gc2.gridy = 2;
        gc2.weightx = 1;
        gc2.weighty = 1;
        gc2.fill = GridBagConstraints.NONE;
        moreThanStandard.add(abaad2, gc2);

        gc2.gridx = 1;
        gc2.gridy = 2;
        gc2.weightx = 1;
        gc2.weighty = 1;
        gc2.fill = GridBagConstraints.NONE;
        moreThanStandard.add(arzTxt2, gc2);

        gc2.gridx = 2;
        gc2.gridy = 2;
        gc2.weightx = 1;
        gc2.weighty = 1;
        gc2.fill = GridBagConstraints.NONE;
        moreThanStandard.add(tulTxt2, gc2);


        GridBagConstraints gc3 = new GridBagConstraints();

        gc3.gridx = 0;
        gc3.gridy = 0;
        gc3.weightx = 1;
        gc3.weighty = 1;
        gc3.fill = GridBagConstraints.NONE;
        noneStandardPanel.add(lessThanStandard, gc3);

        gc3.gridx = 0;
        gc3.gridy = 1;
        gc3.weightx = 1;
        gc3.weighty = 1;
        gc3.fill = GridBagConstraints.NONE;
        noneStandardPanel.add(moreThanStandard, gc3);


        Double[][] noneStandarList = parent.getNoneStandarList();
        if (noneStandarList[0] == null)
            none1.setSelected(true);
        else if (noneStandarList[0].length == 1) {
            metrazh1.setSelected(true);
            metrazhTxt1.setText(noneStandarList[0][0]+"");
        } else if (noneStandarList[0].length == 2) {
            abaad1.setSelected(true);
            arzTxt1.setText(noneStandarList[0][0]+"");
            tulTxt1.setText(noneStandarList[0][1]+"");
        }
        if (noneStandarList[1] == null)
            none2.setSelected(true);
        else if (noneStandarList[1].length == 1) {
            metrazh2.setSelected(true);
            metrazhTxt2.setText(noneStandarList[1][0]+"");
        } else if (noneStandarList[1].length == 2) {
            abaad2.setSelected(true);
            arzTxt2.setText(noneStandarList[1][0]+"");
            tulTxt2.setText(noneStandarList[1][1]+"");
        }
    }

    private Boolean checkNoneStandardForm() {
        boolean checkFlag = false;
        if (metrazh1.isSelected())
            checkFlag = parent.checkIs(metrazhTxt1, Double.MIN_VALUE);
        else if (abaad1.isSelected())
            checkFlag = parent.checkIs(tulTxt1, Double.MIN_VALUE)
                    | parent.checkIs(arzTxt1, Double.MIN_VALUE);
        if (metrazh2.isSelected())
            checkFlag |= parent.checkIs(metrazhTxt2, Double.MIN_VALUE);
        else if (abaad2.isSelected())
            checkFlag |= parent.checkIs(tulTxt2, Double.MIN_VALUE)
                    | parent.checkIs(arzTxt2, Double.MIN_VALUE);
        return checkFlag;
    }

    private Double[][] getNoneStandard() {
        Double[][] noneStandard = new Double[2][];
        if (none1.isSelected())
            noneStandard[0] = null;
        else if (metrazh1.isSelected())
            noneStandard[0] = new Double[]{Double.parseDouble(metrazhTxt1.getText())};
        else if (abaad1.isSelected())
            noneStandard[0] = new Double[]{Double.parseDouble(arzTxt1.getText())
                    , Double.parseDouble(tulTxt1.getText())};

        if (none2.isSelected())
            noneStandard[1] = null;
        else if (metrazh2.isSelected())
            noneStandard[1] = new Double[]{Double.parseDouble(metrazhTxt2.getText())};
        else if (abaad2.isSelected())
            noneStandard[1] = new Double[]{Double.parseDouble(arzTxt2.getText())
                    , Double.parseDouble(tulTxt2.getText())};
        return noneStandard;
    }

    private void initAboutPanel() {
        aboutPanel = new JPanel(new BorderLayout(5, 5));
        JTextArea welcome = new JTextArea("نرم افزار پرینت برچسپ\n" + "نسخه:2.0.1\n\n" +
                "این نرم افزار توسط رضا محمدزاده پیاده سازی شده است. استفاده از این برنامه رایگان بوده و هرگونه سو استفاده از آن حرام است.\n"
                + "راه های ارتباط:\n" + "reza.mohammadzadeh.1997@gmail.com");
        welcome.setName("welcome");
        welcome.setFont(defaultFont);
        welcome.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        welcome.setLineWrap(true);
        welcome.setWrapStyleWord(true);
        welcome.setOpaque(false);
        welcome.setEditable(false);
        welcome.setBorder(new EmptyBorder(20, 20, 20, 20));
        aboutPanel.add(welcome);
    }
}
