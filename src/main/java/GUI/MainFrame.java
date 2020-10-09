package GUI;

import DB.DataBaseManager;
import DB.MyDate;
import DB.SAXParser;
import DS.GlassBuyInfo;
import DS.GlassType;
import org.jdom2.JDOMException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by Reza on 24/08/2017.
 */
public class MainFrame extends JFrame {

    private Font defaultFont;
    private DataBaseManager dataBaseManager;
    private boolean changeMode;
    private int changeID;
    private String homeDir;
    private MainFrame me;
    // buttonPanel Components start
    private JPanel buttonPanel;
    private JButton newList, addToDB, clearDB, printList, option;
    private JTable lists;
    private DefaultTableModel details;
    private ArrayList<String[]> detail;
    // buttonPanel Components end

    // statePanel Components start
    private JPanel statePanel;
    private JLabel metrazh, tedad;
    // statePanel Components end

    // infoPanel Components start
    private JPanel datePanel, namePanel, infoPanel, listButtonPanel;
    private JTextField name, year, month, day, aghlam;
    private JButton afzudan, kastan;
    private String tempAghlam;
    // infoPanel Components end

    // listPanel Components start
    private ArrayList<ListPanel> Lists;
    private JPanel listPanel;
    private JScrollPane listPanelPane;
    private int newHeight;
    // listPanel Components end

    //Settings list start
    private Vector<String> listShishe;
    private Double[][] noneStandarList;
    //Settings list end

    // Listeners start
    private ActionListener listButtonPanelListener;
    private ActionListener buttonPanelListener;
    private FocusListener listButtonPanelFocusListener;
    private ComponentListener listPanelSizeListener;
    private WindowStateListener listPanelStateListener;
    private PropertyChangeListener aghlamListener;
    private FocusListener datePanelFocusListener;
    private MouseListener orderTableMouseListener;
    // Listeners end

    // buttonPanel Components initializer
    private void initButtonPanel() {
        buttonPanel = new JPanel(new BorderLayout(5, 5));
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        JPanel tempPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        tempPanel1.setPreferredSize(new Dimension(187, 275));

        newList = new JButton("لیست جدید");
        newList.setFont(defaultFont);
        newList.setPreferredSize(new Dimension(185, 50));
        newList.addActionListener(buttonPanelListener);
        tempPanel1.add(newList);

        addToDB = new JButton("افزودن به داده ها");
        addToDB.setFont(defaultFont);
        addToDB.setPreferredSize(new Dimension(185, 50));
        addToDB.addActionListener(buttonPanelListener);
        tempPanel1.add(addToDB);

        clearDB = new JButton("حذف کامل داده ها");
        clearDB.setFont(defaultFont);
        clearDB.setPreferredSize(new Dimension(185, 50));
        clearDB.addActionListener(buttonPanelListener);
        tempPanel1.add(clearDB);


        printList = new JButton("چاپ داده ها");
        printList.setFont(defaultFont);
        printList.setPreferredSize(new Dimension(185, 50));
        printList.addActionListener(buttonPanelListener);
        tempPanel1.add(printList);

        option = new JButton("تنظیمات");
        option.setFont(defaultFont);
        option.setPreferredSize(new Dimension(185, 50));
        option.addActionListener(buttonPanelListener);
        tempPanel1.add(option);


        details = new DefaultTableModel();
        details.setColumnIdentifiers(new String[]{"مشتری"});
        lists = new JTable(details) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText("<html><span>" + row + ": </span><font size=\"6\" face=\"tahoma\" color=\"black\">" + detail.get(row)[2] + "عدد-" + String.format("%.2f", Double.parseDouble(detail.get(row)[1])) + "مترمربع" + "</font> </html>");
                }
                return c;
            }
        };
        lists.setFont(defaultFont);
        lists.setColumnSelectionAllowed(false);
        lists.setDragEnabled(false);
        lists.getTableHeader().setReorderingAllowed(false);
        lists.getTableHeader().setResizingAllowed(false);
        lists.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int x = 0; x < lists.getColumnCount(); x++)
            lists.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
        lists.setEnabled(false);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(lists, BorderLayout.CENTER);
        lists.setRowHeight(30);
        lists.getTableHeader().setToolTipText("<html><font size=\"6\" face=\"tahoma\" color=\"red\"> برای تغییر در لیست روی شخص مورد نظر خود کلیک کنید!</font> </html>");
        lists.getTableHeader().setFont(defaultFont);
        lists.addMouseListener(orderTableMouseListener);
        tablePanel.add(lists.getTableHeader(), BorderLayout.NORTH);
        JScrollPane tablePanelPane = new JScrollPane(tablePanel);
        tablePanelPane.getVerticalScrollBar().setUnitIncrement(15);


        buttonPanel.add(tempPanel1, BorderLayout.NORTH);
        buttonPanel.add(tablePanelPane, BorderLayout.CENTER);
    }

    // statePanel Components initializer
    private void initStatePanel() {
        statePanel = new JPanel(new GridLayout(1, 6, 5, 5));

        JLabel metrazh1 = new JLabel("مترمربع");
        metrazh1.setFont(defaultFont);
        metrazh1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statePanel.add(metrazh1);

        metrazh = new JLabel("1.00");
        metrazh.setFont(defaultFont);
        metrazh.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statePanel.add(metrazh);

        JLabel metrazh2 = new JLabel("متراژ:");
        metrazh2.setFont(defaultFont);
        metrazh2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statePanel.add(metrazh2);

        JLabel tedad1 = new JLabel("عدد");
        tedad1.setFont(defaultFont);
        tedad1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statePanel.add(tedad1);

        tedad = new JLabel("1");
        tedad.setFont(defaultFont);
        tedad.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statePanel.add(tedad);

        JLabel tedad2 = new JLabel("تعداد:");
        tedad2.setFont(defaultFont);
        tedad2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statePanel.add(tedad2);

    }

    // infoPanel Components initializer
    private void initInfoPanel() {

        infoPanel = new JPanel(new BorderLayout(5, 5));
        Set<AWTKeyStroke> defaultKeys = infoPanel.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newKeys = new HashSet<>(defaultKeys);
        newKeys.add(KeyStroke.getKeyStroke("pressed ENTER"));
        infoPanel.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);
        namePanel = new JPanel(new BorderLayout(5, 5));

        datePanel = new JPanel(new BorderLayout(5, 5));
        datePanel.setPreferredSize(new Dimension(200, 35));

        listButtonPanel = new JPanel(new BorderLayout(5, 5));
        listButtonPanel.setPreferredSize(new Dimension(250, 35));

        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        infoPanel.add(datePanel, BorderLayout.LINE_START);
        infoPanel.add(namePanel, BorderLayout.CENTER);
        infoPanel.add(listButtonPanel, BorderLayout.LINE_END);

        infoPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);


        afzudan = new JButton("+");
        afzudan.setFont(defaultFont);
        listButtonPanel.add(afzudan, BorderLayout.EAST);
        afzudan.addActionListener(listButtonPanelListener);
        afzudan.setFocusable(false);

        aghlam = new JTextField("1");
        aghlam.setFont(defaultFont);
        aghlam.setHorizontalAlignment(SwingConstants.CENTER);
        listButtonPanel.add(aghlam, BorderLayout.CENTER);
        aghlam.addFocusListener(listButtonPanelFocusListener);

        kastan = new JButton("-");
        kastan.setFont(defaultFont);
        listButtonPanel.add(kastan, BorderLayout.WEST);
        kastan.addActionListener(listButtonPanelListener);
        kastan.setFocusable(false);

        name = new JTextField();
        name.setFont(defaultFont);
        name.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        namePanel.add(name, BorderLayout.CENTER);

        JLabel lblName = new JLabel("نام مشتری:");
        lblName.setFont(defaultFont);
        lblName.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        namePanel.add(lblName, BorderLayout.EAST);

        year = new JTextField(MyDate.nowDateShamsi().getYear() + "");
        year.setPreferredSize(new Dimension(55, 30));
        year.setFont(defaultFont);
        year.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        year.addFocusListener(datePanelFocusListener);
        tempPanel.add(year);

        month = new JTextField(MyDate.nowDateShamsi().getMonth() + "");
        month.setFont(defaultFont);
        month.setPreferredSize(new Dimension(35, 30));
        month.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        month.addFocusListener(datePanelFocusListener);
        tempPanel.add(month);

        day = new JTextField(MyDate.nowDateShamsi().getDay() + "");
        day.setFont(defaultFont);
        day.setPreferredSize(new Dimension(35, 30));
        day.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        day.addFocusListener(datePanelFocusListener);
        tempPanel.add(day);
        datePanel.add(tempPanel, BorderLayout.CENTER);

        JLabel lblDate = new JLabel("تاریخ :");
        lblDate.setFont(defaultFont);
        lblDate.setPreferredSize(new Dimension(50, 35));
        lblDate.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        datePanel.add(lblDate, BorderLayout.EAST);

    }

    // infoPanel Components initializer
    private void initListPanel() {
        listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        Lists = new ArrayList<>();
        listShishe = new Vector<>();
        Lists.add(new ListPanel(defaultFont, newHeight, listShishe, Lists.size(),noneStandarList));
        Lists.get(Lists.size() - 1).addPropertyChangeListener(aghlamListener);
        listPanel.add(Lists.get(Lists.size() - 1));
        addWindowStateListener(listPanelStateListener);
    }

    // Listeners initializer
    private void initListeners() {
        buttonPanelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(option)) {
                    startSetting();
                } else if (e.getSource().equals(addToDB)) {
                    if (checkInformations()) {
                        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        panel.setPreferredSize(new Dimension(310, 80));

                        JLabel label = new JLabel(changeMode ? "تغییرات ذخیره شوند؟" : "اطلاعات اضافه شود؟");
                        label.setFont(defaultFont);
                        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                        label.setPreferredSize(new Dimension(310, 35));

                        panel.add(label);
                        String[] options = {"بله", "خیر"};
                        JOptionPane myPane = new JOptionPane();
                        myPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                        myPane.setMessage(panel);
                        myPane.setOptions(options);
                        myPane.setInitialValue("بله");
                        myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        JDialog myDialog = myPane.createDialog(me, "هشدار");
                        myDialog.setVisible(true);
                        Object answer = myPane.getValue();
                        if (answer.equals("بله")) {
                            if (changeMode) {
                                dataBaseManager.editDB(changeID, getGlassBuyInfo());
                                endChangeMode();
                            } else {
                                dataBaseManager.addToDB(getGlassBuyInfo());
                            }
                            newList();
                            syncDBList();
                        }
                    } else {
                        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        panel.setPreferredSize(new Dimension(310, 80));

                        JLabel label = new JLabel("مشکلی در فرم ها وجود دارد!!");
                        label.setFont(defaultFont);
                        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                        label.setPreferredSize(new Dimension(310, 35));

                        JLabel label1 = new JLabel("لطفا پس از اصلاح مشکل تلاش کنید.");
                        label1.setFont(defaultFont);
                        label1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        label1.setAlignmentX(Component.RIGHT_ALIGNMENT);
                        label1.setPreferredSize(new Dimension(310, 35));

                        panel.add(label);
                        panel.add(label1);
                        String[] options = {"OK"};
                        JOptionPane myPane = new JOptionPane();
                        myPane.setMessageType(JOptionPane.ERROR_MESSAGE);
                        myPane.setMessage(panel);
                        myPane.setOptions(options);
                        myPane.setInitialValue("OK");
                        myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        JDialog myDialog = myPane.createDialog(me, "خطا");
                        myDialog.setVisible(true);
                        Object answer = myPane.getValue();
                    }
                } else if (e.getSource().equals(clearDB)) {
                    JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                    panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    panel1.setPreferredSize(new Dimension(350, 35));

                    JLabel label1 = new JLabel("آیا میخواهید همه ی اطلاعات حذف شود؟");
                    label1.setFont(defaultFont);
                    label1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    label1.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    label1.setPreferredSize(new Dimension(350, 35));
                    panel1.add(label1);

                    String[] options1 = {"Yes", "No"};
                    JOptionPane myPane1 = new JOptionPane();
                    myPane1.setMessageType(JOptionPane.ERROR_MESSAGE);
                    myPane1.setMessage(panel1);
                    myPane1.setOptions(options1);
                    myPane1.setInitialValue("Yes");
                    myPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    JDialog myDialog1 = myPane1.createDialog(me, "حذف کردن همه داده ها");
                    myDialog1.setVisible(true);
                    Object answer1 = myPane1.getValue();
                    if (answer1.equals("Yes")) {
                        dataBaseManager.deleteAll();
                        syncDBList();
                    }
                } else if (e.getSource().equals(newList)) {
                    JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                    panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    panel1.setPreferredSize(new Dimension(500, 35));

                    JLabel label1 = new JLabel("با این کار اطلاعات وارد شده حذف می شوند ادامه میدهید؟");
                    label1.setFont(defaultFont);
                    label1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    label1.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    label1.setPreferredSize(new Dimension(500, 35));
                    panel1.add(label1);

                    String[] options1 = {"Yes", "No"};
                    JOptionPane myPane1 = new JOptionPane();
                    myPane1.setMessageType(JOptionPane.ERROR_MESSAGE);
                    myPane1.setMessage(panel1);
                    myPane1.setOptions(options1);
                    myPane1.setInitialValue("Yes");
                    myPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    JDialog myDialog1 = myPane1.createDialog(me, "لیست جدید");
                    myDialog1.setVisible(true);
                    Object answer1 = myPane1.getValue();
                    if (answer1.equals("Yes")) {
                        newList();
                    }
                } else if (e.getSource().equals(printList)) {
                    startPrint();
                }
            }
        };

        aghlamListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("metrazh")) {
                    syncMetrazh();
                } else if (e.getPropertyName().equals("tedad")) {
                    syncTedad();
                } else if (e.getPropertyName().equals("focus")) {
                    Component focusedComponent = Lists.get((Integer) e.getOldValue());
                    listPanel.scrollRectToVisible(focusedComponent.getBounds(null));
                    repaint();
                }
            }
        };

        listButtonPanelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(afzudan)) {
                    aghlam.setText(Integer.parseInt(aghlam.getText()) + 1 + "");
                    syncAghlamPanels();
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
                aghlam.selectAll();
                if (e.getSource().equals(aghlam))
                    tempAghlam = aghlam.getText();

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource().equals(aghlam))
                    if (!aghlam.getText().matches("[\\d]+"))
                        aghlam.setText(tempAghlam);
                    else if (Integer.parseInt(aghlam.getText()) < 1)
                        aghlam.setText(tempAghlam);
                    else if (!aghlam.getText().equals(tempAghlam)) {
                        syncAghlamPanels();
                    }

            }
        };

        datePanelFocusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField) e.getSource()).selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (!MyDate.isValidDateShamsi(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()), Integer.parseInt(day.getText()))) {
                        throw new Exception();
                    } else {
                        year.setBackground(Color.white);
                        year.setToolTipText("");
                        month.setBackground(Color.white);
                        month.setToolTipText("");
                        day.setBackground(Color.white);
                        day.setToolTipText("");
                    }
                } catch (Exception r) {
                    year.setBackground(Color.RED);
                    year.setToolTipText("<html><font size=\"6\" face=\"tahoma\" color=\"red\">تاریخ قابل قبول نیست!</font></html>");
                    month.setBackground(Color.RED);
                    month.setToolTipText("<html><font size=\"6\" face=\"tahoma\" color=\"red\">تاریخ قابل قبول نیست!</font></html>");
                    day.setBackground(Color.RED);
                    day.setToolTipText("<html><font size=\"6\" face=\"tahoma\" color=\"red\">تاریخ قابل قبول نیست!</font></html>");
                }
            }
        };

        listPanelSizeListener = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                newHeight = (int) listPanelPane.getSize().getHeight() - 25;
                for (ListPanel temp : Lists)
                    temp.setPreferredSize(new Dimension(250, newHeight));
                revalidate();
                repaint();
            }
        };

        listPanelStateListener = new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                super.windowStateChanged(e);
                // maximized
                if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    newHeight = (int) listPanelPane.getMaximumSize().getHeight() - 25;
                    for (ListPanel temp : Lists)
                        temp.setPreferredSize(new Dimension(250, newHeight));
                    revalidate();
                    repaint();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                dataBaseManager.closeDataBase();
            }

        };
        orderTableMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = lists.rowAtPoint(e.getPoint());
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                panel.setPreferredSize(new Dimension(350, 35));

                JLabel label = new JLabel("عملیات مورد نظر خود را انتخاب کنید؟");
                label.setFont(defaultFont);
                label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                label.setPreferredSize(new Dimension(350, 35));

                panel.add(label);
                String[] options = {"تغییر در لیست", "حذف", "لغو"};
                JOptionPane myPane = new JOptionPane();
                myPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                myPane.setMessage(panel);
                myPane.setOptions(options);
                myPane.setFont(defaultFont);
                myPane.setInitialValue("لغو");
                myPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                JDialog myDialog = myPane.createDialog(me, "انتخاب عملیات");
                myDialog.setVisible(true);
                Object answer = myPane.getValue();
                if (answer.equals("حذف")) {
                    JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                    panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    panel1.setPreferredSize(new Dimension(350, 35));

                    JLabel label1 = new JLabel("آیا میخواهید لیست مورد نظر حذف شود؟");
                    label1.setFont(defaultFont);
                    label1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    label1.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    label1.setPreferredSize(new Dimension(350, 35));
                    panel1.add(label1);

                    String[] options1 = {"Yes", "No"};
                    JOptionPane myPane1 = new JOptionPane();
                    myPane1.setMessageType(JOptionPane.ERROR_MESSAGE);
                    myPane1.setMessage(panel1);
                    myPane1.setOptions(options1);
                    myPane1.setInitialValue("Yes");
                    myPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    JDialog myDialog1 = myPane1.createDialog(me, "حذف کردن لیست");
                    myDialog1.setVisible(true);
                    Object answer1 = myPane1.getValue();
                    if (answer1.equals("Yes")) {
                        dataBaseManager.deleteOrder(Integer.parseInt(detail.get(row)[3]));
                        syncDBList();
                    }
                } else if (answer.equals("تغییر در لیست")) {
                    JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                    panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    panel1.setPreferredSize(new Dimension(550, 35));

                    JLabel label1 = new JLabel();
                    if (changeMode)
                        label1.setText("با این کار تغییرات در لیست باز ذخیره نمی شوند ادامه می دهید؟");
                    else
                        label1.setText("با این کار لیست ذخیره نشده حذف می شود ادامه می دهید؟");
                    label1.setFont(defaultFont);
                    label1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    label1.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    label1.setPreferredSize(new Dimension(550, 35));
                    panel1.add(label1);

                    String[] options1 = {"Yes", "No"};
                    JOptionPane myPane1 = new JOptionPane();
                    myPane1.setMessageType(JOptionPane.ERROR_MESSAGE);
                    myPane1.setMessage(panel1);
                    myPane1.setOptions(options1);
                    myPane1.setInitialValue("Yes");
                    myPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    JDialog myDialog1 = myPane1.createDialog(me, "تغییر کردن لیست");
                    myDialog1.setVisible(true);
                    Object answer1 = myPane1.getValue();
                    if (answer1.equals("Yes")) {
                        System.out.println(detail.get(row)[3]);
                        restoreList(Integer.parseInt(detail.get(row)[3]));
                    }
                }
            }

        };

    }

    private void startPrint() {
        try {
            SAXParser getLastPrint = new SAXParser(homeDir + "Settings.xml");
            PrintPage printPage = new PrintPage(this, getLastPrint.getLastPrint(), getLastPrint.getLastTitle(), dataBaseManager.getOrderList());
            setEnabled(false);
            printPage.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getPropertyName().equals("print")) {
                        String[] str = (String[]) e.getOldValue();
                        saveLastPrint(str[0], str[1]);
                        dataBaseManager.printListMaker((List<GlassBuyInfo>) e.getNewValue(), str[1]);
                        boolean isWindows = OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Windows);
                        if (isWindows) {
                            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files (x86)\\Labeljoy 5\\Labeljoy5.exe",
                                    "/p",
                                    str[0]);
                            pb.redirectErrorStream(true);
                            try {
                                pb.start();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean checkInformations() {
        boolean flag = true;
        if (name.getText().isEmpty()) {
            name.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            flag = false;
        } else
            name.setBorder(new JTextField().getBorder());
        if (year.getText().isEmpty() || !year.getText().matches("[\\d]+") || year.getText().length() < 4) {
            year.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            flag = false;
        } else
            year.setBorder(new JTextField().getBorder());
        if (month.getText().isEmpty() || !month.getText().matches("[\\d]+") || month.getText().length() > 2) {
            month.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            flag = false;
        } else
            month.setBorder(new JTextField().getBorder());
        if (day.getText().isEmpty() || !day.getText().matches("[\\d]+") || day.getText().length() > 2) {
            day.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            flag = false;
        } else
            day.setBorder(new JTextField().getBorder());
        for (ListPanel lp : Lists) {
            if (!lp.checkInformations())
                flag = false;
        }
        return flag;
    }

    public GlassBuyInfo getGlassBuyInfo() {
        if (checkInformations()) {
            GlassBuyInfo gbi = new GlassBuyInfo(name.getText(), new MyDate(String.format("%4s/%2s/%2s", year.getText(), month.getText(), day.getText()), false));
            for (ListPanel lp : Lists) {
                GlassType gt = lp.getGlassType();
                if (gt == null)
                    return null;
                gbi.getList().add(gt);
            }
            return gbi;
        }
        return null;
    }

    public MainFrame() throws HeadlessException {
        super("منو اصلی");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));

        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        defaultFont = new Font("tahoma", Font.PLAIN, 20);
        setLayout(new BorderLayout(5, 5));
        getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, this.getBackground()));
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        changeMode = false;
        homeDir = System.getProperty("user.home") + "/LabelMaker/";
        initListeners();
        initStatePanel();
        mainPanel.add(statePanel, BorderLayout.SOUTH);
        me = this;


        URL iconURL = getClass().getResource("/MainIcon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        initInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        initListPanel();
        listPanelPane = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        newHeight = (int) listPanelPane.getSize().getHeight() - 25;
        addComponentListener(listPanelSizeListener);
        listPanelPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        listPanelPane.setWheelScrollingEnabled(true);
        listPanelPane.getHorizontalScrollBar().setUnitIncrement(30);
        mainPanel.add(listPanelPane, BorderLayout.CENTER);

        initButtonPanel();
        add(buttonPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);


        startApp();
        dataBaseManager = new DataBaseManager(homeDir);
        syncDBList();

        setVisible(true);
    }

    private void syncTedad() {
        int t = 0;
        for (ListPanel lp : Lists) {
            t += lp.getTedad();
        }
        tedad.setText(String.format("%d", t));
    }

    private void syncMetrazh() {
        double m = 0;
        for (ListPanel lp : Lists)
            m += lp.getMetrazh();
        metrazh.setText(String.format("%.2f", m / 10000));
    }

    private void startSetting() {
        setEnabled(false);
        Setting tempSetting = new Setting(this);
        tempSetting.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals("listShishe")) {
                syncListShishe((Vector<String>) e.getNewValue());
                saveListShishe();
            } else if (e.getPropertyName().equals("NoneStandard")) {
                syncNoneStandards((Double[][]) e.getNewValue());
                saveNoneStandards();
            }
        });

    }

    private void saveNoneStandards() {
        try {
            SAXParser temp = new SAXParser(homeDir + "Settings.xml");
            temp.makeXML(getNoneStandarList());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveListShishe() {
        try {
            SAXParser temp = new SAXParser(homeDir + "Settings.xml");
            temp.makeXML(getListShishe());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLastPrint(String file, String title) {
        try {
            SAXParser temp = new SAXParser(homeDir + "Settings.xml");
            temp.makeXML(file, title);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector<String> getListShishe() {
        return listShishe;
    }

    public Double[][] getNoneStandarList() {
        return noneStandarList;
    }

    private void startApp() {
        File f = new File(homeDir + "Settings.xml");
        if (!f.exists()) {
            restoreDefaultSettings();
        }
        try {
            SAXParser temp = new SAXParser(f.getAbsolutePath());
            syncListShishe(temp.getList());
            syncNoneStandards(temp.getNoneStandards());
        } catch (JDOMException e) {
            JOptionPane.showMessageDialog(null, "فایل تنظیمات دچار مشکل شده است! آن را پاک کنید.", "خطا", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "فایل تنظیمات دچار مشکل شده است! آن را پاک کنید.", "خطا", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restoreDefaultSettings() {
        File f = new File(homeDir + "Settings.xml");
        if (!(new File(homeDir).exists()))
            new File(homeDir).mkdir();
        try {
            Path path = Paths.get(homeDir + "Settings.xml");
            Files.copy(getClass().getResourceAsStream("/Settings.xml"), path, REPLACE_EXISTING);
            instalFont();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "متاسفانه خطایی رخ داده است! سازنده برنامه رو مطلع کنید", "خطا", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void instalFont() {
        try {
            OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
            Path path2 = Paths.get(homeDir + "IRKoodak.ttf");
            Files.copy(getClass().getResourceAsStream("/IRKoodak.ttf"), path2, REPLACE_EXISTING);
            ProcessBuilder pb = null;
            switch (ostype) {
                case Windows:
                    Path path3 = Paths.get(homeDir + "instalFont.bat");
                    Files.copy(getClass().getResourceAsStream("/instalFont.bat"), path3, REPLACE_EXISTING);
                    pb = new ProcessBuilder("cmd", "/c", homeDir + "instalFont.bash");
                    break;
                case MacOS:
                    break;
                case Linux:
                    Path path4 = Paths.get(homeDir + "instalFont.bash");
                    Files.copy(getClass().getResourceAsStream("/instalFont.bash"), path4, REPLACE_EXISTING);
                    pb = new ProcessBuilder("bash", "-c", homeDir + "bash instalFont.bash");
                    break;
                case Other:
                    break;
            }
            if (pb != null)
                pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "متاسفانه خطایی رخ داده است! سازنده برنامه رو مطلع کنید", "خطا", JOptionPane.ERROR_MESSAGE);
        }
    }

    //check
    private void syncListShishe(Vector<String> list) {
        listShishe = list;
        Lists.forEach(l -> l.syncShisheLiast(list));
    }

    private void syncNoneStandards(Double[][] noneStandarList) {
        this.noneStandarList = noneStandarList;
        Lists.forEach(l -> l.syncNoneStandard(noneStandarList));
    }

    private void restoreList(int orderID) {
        startChangeMode();
        changeID = orderID;
        GlassBuyInfo gbi = dataBaseManager.getOrderIformations(orderID);
        name.setText(gbi.getCustomer());
        year.setText(gbi.getDate().getYear() + "");
        month.setText(gbi.getDate().getMonth() + "");
        day.setText(gbi.getDate().getDay() + "");
        aghlam.setText(gbi.getList().size() + "");
        syncAghlamPanels();
        for (int i = 0; i < gbi.getList().size(); i++) {
            Lists.get(i).setAttribute(gbi.getList().get(i));
        }
        syncMetrazh();
        syncTedad();
    }

    private void syncAghlamPanels() {
        int newN = Integer.parseInt(aghlam.getText());
        int oldN = Lists.size();
        for (; newN < oldN; newN++) {
            listPanel.remove(Lists.get(Lists.size() - 1));
            Lists.remove(Lists.size() - 1);
        }
        for (; newN > oldN; oldN++) {
            Lists.add(new ListPanel(defaultFont, newHeight, listShishe, Lists.size(), noneStandarList));
            Lists.get(Lists.size() - 1).addPropertyChangeListener(aghlamListener);
            listPanel.add(Lists.get(Lists.size() - 1));
        }
        syncMetrazh();
        syncTedad();
        revalidate();
        repaint();
    }

    private void syncDBList() {
        details.setRowCount(0);
        List<GlassBuyInfo> gbis = dataBaseManager.getOrderList();
        detail = new ArrayList<>();
        int i = 1;
        for (GlassBuyInfo gbi : gbis)
            detail.add(new String[]{gbi.getCustomer(), (gbi.getMetrazh() / 10000) + "", gbi.getTedad() + "", gbi.getID() + ""});
        for (String[] temp1 : detail) {
            details.addRow(new Object[]{temp1[0]});
        }
    }

    public void newList() {
        endChangeMode();
        name.setText("");
        year.setText(MyDate.nowDateShamsi().getYear() + "");
        month.setText(MyDate.nowDateShamsi().getMonth() + "");
        day.setText(MyDate.nowDateShamsi().getDay() + "");
        aghlam.setText("1");
        syncAghlamPanels();
        Lists.get(0).setNewPanel();
    }

    private void startChangeMode() {
        changeMode = true;
        addToDB.setText("ذخیره تغییرات");
    }

    private void endChangeMode() {
        changeMode = false;
        addToDB.setText("افزودن به داده ها");
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MainFrame temp = new MainFrame();
    }

    void addIsListener(JTextField txt, Object t) {
        DocumentListener dl = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkIs(txt, t);
            }

            public void removeUpdate(DocumentEvent e) {
                checkIs(txt, t);
            }

            public void insertUpdate(DocumentEvent e) {
                checkIs(txt, t);
            }
        };
        txt.getDocument().addDocumentListener(dl);

    }

    boolean checkIs(JTextField txt, Object Type) {
        String str = txt.getText();
        boolean err = false;
        if (str.isEmpty())
            err = true;
        if (Type instanceof Integer)
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                err = true;
            }
        else if (Type instanceof Double)
            try {
                Double.parseDouble(str);
            } catch (NumberFormatException e) {
                err = true;
            }
        if (err) {
            txt.setBorder(BorderFactory.createLineBorder(Color.RED));
            repaint();
        } else {
            txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            repaint();
        }
        return err;
    }
}

final class OsCheck {
    /**
     * types of Operating Systems
     */
    public enum OSType {
        Windows, MacOS, Linux, Other
    }

    ;

    // cached result of OS detection
    protected static OSType detectedOS;

    /**
     * detect the operating system from the os.name System property and cache
     * the result
     *
     * @returns - the operating system detected
     */
    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}
