package GUI;

import DS.GlassBuyInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rezat on 12/09/2017.
 */
public class PrintPage extends JFrame {

    private JPanel adressPanel;
    private JPanel titlePanel;
    private JButton print, cancel;
    private Font defaultFont;
    ActionListener buttonListener;
    JButton find;
    JTextField path;
    JTextField title;
    List<GlassBuyInfo> list;
    JCheckBox[] jcb;

    public PrintPage(Component parent, String pathStr, String titleStr, List<GlassBuyInfo> list) throws HeadlessException {
        super("تنظیمات");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(600, 400);
        setResizable(false);
        defaultFont = new Font("tahoma", Font.PLAIN, 20);
        setLocationRelativeTo(parent);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(5, 5));

        this.list = list;
        buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(print)) {
                    if (path.getText().isEmpty() || !new File(path.getText()).exists() || !path.getText().endsWith("lpa")) {
                        if (getLabelFile()) {
                            print();
                        }
                    } else print();
                }
                dispose();
            }
        };

        initAdresPanel(pathStr, titleStr);
        initListPanel();
        JPanel tempPanel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        print = new JButton("چاپ لیست");
        print.setFont(defaultFont);
        print.setPreferredSize(new Dimension(185, 50));
        print.addActionListener(buttonListener);
        tempPanel1.add(print);

        cancel = new JButton("لغو");
        cancel.setFont(defaultFont);
        cancel.setPreferredSize(new Dimension(185, 50));
        cancel.addActionListener(buttonListener);
        tempPanel1.add(cancel);


        add(tempPanel1, BorderLayout.SOUTH);


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
                parent.setEnabled(true);
                parent.requestFocus();
            }
        });
        setVisible(true);
    }

    private void print() {
        ArrayList<GlassBuyInfo> l = new ArrayList<>();
        for (int i = 0; i < jcb.length; i++)
            if (jcb[i].isSelected())
                l.add(list.get(i));
        firePropertyChange("print", new String[]{path.getText(), title.getText()}, l);
    }

    private void initListPanel() {
        JPanel listPanel = new JPanel(new GridLayout(list.size(), 1));
        listPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JScrollPane js = new JScrollPane(listPanel);
        js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        js.getVerticalScrollBar().setUnitIncrement(10);
        jcb = new JCheckBox[list.size()];
        int i = 0;
        for (GlassBuyInfo gbi : list) {
            jcb[i] = new JCheckBox(gbi.getCustomer());
            jcb[i].setFont(defaultFont);
            jcb[i].setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jcb[i].setSelected(true);
            listPanel.add(jcb[i]);
            i++;
        }
        add(js, BorderLayout.CENTER);
    }

    private void initAdresPanel(String pathStr, String titleStr) {
        JPanel tempPanel = new JPanel(new BorderLayout(5, 5));

        adressPanel = new JPanel(new BorderLayout(5, 5));
        path = new JTextField(pathStr);
        path.setFont(defaultFont);
        adressPanel.add(path, BorderLayout.CENTER);


        find = new JButton("فایل برچسب");
        find.setFont(defaultFont);
        find.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLabelFile();
            }
        });
        adressPanel.add(find, BorderLayout.EAST);
        tempPanel.add(adressPanel, BorderLayout.NORTH);

        titlePanel = new JPanel(new BorderLayout(5, 5));
        title = new JTextField(titleStr);
        title.setFont(defaultFont);
        title.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        titlePanel.add(title, BorderLayout.CENTER);


        JLabel titleLbl = new JLabel("عنوان برچسب");
        titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        titleLbl.setFont(defaultFont);
        titleLbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        titlePanel.add(titleLbl, BorderLayout.EAST);
        tempPanel.add(titlePanel, BorderLayout.SOUTH);
        add(tempPanel, BorderLayout.NORTH);
    }

    private boolean getLabelFile() {
        JFileChooser jf = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Label FILES", "lpa", "label");
        jf.setFileFilter(filter);
        Object answer = jf.showOpenDialog(this);
        if (answer.equals(JFileChooser.APPROVE_OPTION)) {
            path.setText(jf.getSelectedFile().getAbsolutePath());
            return true;
        }
        return false;
    }

}
