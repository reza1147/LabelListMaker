package DB;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class SAXParser {

    private SAXBuilder saxBuilder;
    private File inputFile;
    private Document document;
    private Element classElement;

    public SAXParser(String Address) throws JDOMException, IOException {
        inputFile = new File(Address);
        if (!inputFile.exists())
            inputFile.createNewFile();
    }

    public void makeXML(Vector<String> shisheTypeList) {
        try {
            document = new Document();
            document.setRootElement(new Element("Setting"));
            addShisheList(shisheTypeList);

            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            XMLOutputter xmlOutput = new XMLOutputter(format);
            xmlOutput.output(document, new FileOutputStream(inputFile.getAbsolutePath()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeXML(String file, String title) {
        try {
            document = new Document();
            document.setRootElement(new Element("LabelType"));
            Element fileAdress = new Element("LabelFileAdress");
            fileAdress.setText(file);
            document.getRootElement().addContent(fileAdress);
            Element labelTitle = new Element("LabelTitle");
            labelTitle.setText(title);
            document.getRootElement().addContent(labelTitle);
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            XMLOutputter xmlOutput = new XMLOutputter(format);
            xmlOutput.output(document, new FileOutputStream(inputFile.getAbsolutePath()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addShisheList(Vector<String> shisheTypeList) {
        Element glassTypes = new Element("GlassTypes");
        document.getRootElement().addContent(glassTypes);
        Element[] glassTypess = new Element[shisheTypeList.size()];
        for (int i = 0; i < glassTypess.length; i++) {
            glassTypess[i] = new Element("Item");
            glassTypess[i].setText(shisheTypeList.get(i));
            glassTypes.addContent(glassTypess[i]);
        }
    }


    public boolean convert() throws JDOMException, IOException {
        saxBuilder = new SAXBuilder();
        document = saxBuilder.build(inputFile);
        if (!document.getRootElement().getName().equals("Setting")) {
            return false;
        }
        classElement = document.getRootElement();
        List<Element> itemList = classElement.getChildren();
        if (itemList.isEmpty()) {
            return false;
        }
        if (classElement.getChild("GlassTypes") == null) {
            return false;
        }
        return true;
    }

    public Vector<String> getList() {
        List<Element> glassTypes = classElement.getChild("GlassTypes").getChildren();
        Vector<String> temp = new Vector<>();
        for (Element alp : glassTypes) {
            temp.addElement(alp.getValue());
        }
        return temp;
    }


    public String getLastPrint() {
        saxBuilder = new SAXBuilder();
        try {
            document = saxBuilder.build(inputFile);
            if (!document.getRootElement().getName().equals("LabelType")) {
                return "";
            }
            classElement = document.getRootElement();
            if (classElement.getChild("LabelFileAdress") == null) {
                return "";
            }
            return classElement.getChild("LabelFileAdress").getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getLastTitle() {
        saxBuilder = new SAXBuilder();
        try {
            document = saxBuilder.build(inputFile);
            if (!document.getRootElement().getName().equals("LabelType")) {
                return "";
            }
            classElement = document.getRootElement();
            if (classElement.getChild("LabelTitle") == null) {
                return "";
            }
            return classElement.getChild("LabelTitle").getText();
        } catch (Exception e) {
            return "";
        }
    }
}
