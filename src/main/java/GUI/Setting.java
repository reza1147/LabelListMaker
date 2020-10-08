package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
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

    private MainFrame parent;
    private boolean anyThingChange, saveChangesB;
    private JButton saveChanges, cansel;
    private Font defaultFont;

    // listTab Components start
    private JPanel listPanel;
    private JButton up, down, delete, add, edit;
    private JList<String> shisheList;
    private DefaultListModel<String> list;
    private ActionListener buttonPanelListener, buttonListener;
    private ListSelectionListener listSelectionListener;
    // listTab Components end

    // AboutTab Components start
    private JPanel aboutPanel;
    // AboutTab Components end


    // NonStandardTab Components start
    private JPanel noneStandardPanel;
    // NonStandardTab Components end

    // infoPanel Components initializer
    private void initListPanel() {
        listPanel = new JPanel(new BorderLayout(5, 5));
        listPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel tempPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        tempPanel1.setPreferredSize(new Dimension(50, 330));

        listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (((JList) e.getSource()).getSelectedIndex() != -1) {
                    edit.setEnabled(true);
                    delete.setEnabled(true);
                    if (((JList) e.getSource()).getSelectedIndex() != 0)
                        up.setEnabled(true);
                    else
                        up.setEnabled(false);
                    if (((JList) e.getSource()).getSelectedIndex() != ((JList) e.getSource()).getLastVisibleIndex())
                        down.setEnabled(true);
                    else
                        down.setEnabled(false);
                } else {
                    edit.setEnabled(false);
                    delete.setEnabled(false);
                    up.setEnabled(false);
                    down.setEnabled(false);
                }
            }
        };

        buttonPanelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    public Setting(Component parent) throws HeadlessException {
        super("تنظیمات");
        this.parent = (MainFrame) parent;
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

        buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(saveChanges)) {
                    saveChangesB = true;
                }
                dispose();
            }
        };

        saveChanges = new JButton("ذخیره تغییرات");
        saveChanges.setFont(defaultFont);
        saveChanges.setPreferredSize(new Dimension(185, 50));
        saveChanges.addActionListener(buttonListener);
        tempPanel1.add(saveChanges);

        cansel = new JButton("لغو");
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
                    Vector<String> newShisheList = new Vector<String>();
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
                        JDialog myDialog = myPane.createDialog(parent, "حذف کردن شیشه");
                        myDialog.setVisible(true);
                        Object answer = myPane.getValue();
                        if (answer.equals("Yes"))
                            firePropertyChange("listShishe", ((MainFrame) parent).getListShishe(), newShisheList);
                    }
                }
                parent.setEnabled(true);
                parent.requestFocus();
            }
        });
        setVisible(true);
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

        JRadioButton none1 = new JRadioButton("خاموش");
        none1.setFont(defaultFont);

        JRadioButton metrazh1 = new JRadioButton("بر اساس متراژ");
        metrazh1.setFont(defaultFont);

        JRadioButton abaad1 = new JRadioButton("بر اساس ابعاد");
        abaad1.setFont(defaultFont);

        ButtonGroup bg = new ButtonGroup();
        bg.add(none1);
        bg.add(metrazh1);
        bg.add(abaad1);


        JTextField metrazhTxt1 = new JTextField();
        metrazhTxt1.setFont(defaultFont);
        metrazhTxt1.setUI(new HintTextFieldUI("متراژ", true, Color.GRAY));
        parent.addIsListener(metrazhTxt1, Double.MIN_VALUE);

        JTextField arzTxt1 = new JTextField();
        arzTxt1.setFont(defaultFont);
        arzTxt1.setUI(new HintTextFieldUI("عرض", true, Color.GRAY));
        parent.addIsListener(arzTxt1, Double.MIN_VALUE);

        JTextField tulTxt1 = new JTextField();
        tulTxt1.setFont(defaultFont);
        tulTxt1.setUI(new HintTextFieldUI("طول", true, Color.GRAY));
        parent.addIsListener(tulTxt1, Double.MIN_VALUE);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        lessThanStandard.add(none1, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        lessThanStandard.add(metrazh1, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        lessThanStandard.add(metrazhTxt1, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        lessThanStandard.add(abaad1, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        lessThanStandard.add(arzTxt1, gc);

        gc.gridx = 2;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        lessThanStandard.add(tulTxt1, gc);


        GridBagConstraints gc2 = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.BOTH;
        noneStandardPanel.add(lessThanStandard, gc2);
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
