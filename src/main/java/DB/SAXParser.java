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
        if (inputFile.exists()) {
            saxBuilder = new SAXBuilder();
            document = saxBuilder.build(inputFile);
            if (!document.getRootElement().getName().equals("Setting"))
                throw new IOException("Dont match Setting.xml");
            classElement = document.getRootElement();
        } else {
            inputFile.createNewFile();
            document = new Document();
            classElement = new Element("Setting");
        }
    }

    public void makeXML(Vector<String> shisheTypeList) {
        try {
            classElement.removeChild("GlassTypes");
            Element glassTypes = new Element("GlassTypes");
            shisheTypeList.forEach(v -> {
                Element item = new Element("Item");
                item.setText(v);
                glassTypes.addContent(item);
            });
            classElement.addContent(glassTypes);
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
            classElement.removeChild("LabelType");
            Element root = new Element("LabelType");
            Element fileAdress = new Element("LabelFileAdress");
            fileAdress.setText(file);
            root.addContent(fileAdress);
            Element labelTitle = new Element("LabelTitle");
            labelTitle.setText(title);
            root.addContent(labelTitle);
            classElement.addContent(root);
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            XMLOutputter xmlOutput = new XMLOutputter(format);
            xmlOutput.output(document, new FileOutputStream(inputFile.getAbsolutePath()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeXML(Double[][] noneStandarList) {
        try {
            classElement.removeChild("NoneStandard");
            Element root = new Element("NoneStandard");
            Element less = new Element("Less");
            if (noneStandarList[0] == null)
                less.setAttribute("active", "false");
            else if (noneStandarList[0].length == 1) {
                less.setAttribute("active", "true");
                Element metrazh = new Element("metrazh");
                metrazh.setText(String.valueOf(noneStandarList[1][0]));
                less.addContent(metrazh);
            } else if (noneStandarList[0].length == 2) {
                less.setAttribute("active", "true");
                Element arz = new Element("arz");
                arz.setText(String.valueOf(noneStandarList[1][0]));
                Element tul = new Element("tul");
                tul.setText(String.valueOf(noneStandarList[1][1]));
                less.addContent(arz);
                less.addContent(tul);
            }
            root.addContent(less);

            Element more = new Element("More");
            if (noneStandarList[1] == null)
                more.setAttribute("active", "false");
            else if (noneStandarList[1].length == 1) {
                more.setAttribute("active", "true");
                Element metrazh = new Element("metrazh");
                metrazh.setText(String.valueOf(noneStandarList[1][0]));
                more.addContent(metrazh);
            } else if (noneStandarList[1].length == 2) {
                more.setAttribute("active", "true");
                Element arz = new Element("arz");
                arz.setText(String.valueOf(noneStandarList[1][0]));
                Element tul = new Element("tul");
                tul.setText(String.valueOf(noneStandarList[1][1]));
                more.addContent(arz);
                more.addContent(tul);
            }
            root.addContent(more);
            classElement.addContent(root);
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            XMLOutputter xmlOutput = new XMLOutputter(format);
            xmlOutput.output(document, new FileOutputStream(inputFile.getAbsolutePath()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<String> getList() {
        List<Element> glassTypes = classElement.getChild("GlassTypes").getChildren();
        Vector<String> temp = new Vector<>();
        glassTypes.forEach(e -> temp.addElement(e.getValue()));
        return temp;
    }

    public Double[][] getNoneStandards() {
        Double[][] noneStandard = new Double[2][];
        Element root = classElement.getChild("NoneStandard");
        Element less = root.getChild("Less");
        if (!Boolean.parseBoolean(less.getAttributeValue("active")))
            noneStandard[0] = null;
        else if (less.getChildren().size()==1)
            noneStandard[0] = new Double[]{Double.parseDouble(less.getChild("metrazh").getText())};
        else if (less.getChildren().size()==2)
            noneStandard[0] = new Double[]{Double.parseDouble(less.getChild("arz").getText())
                    , Double.parseDouble(less.getChild("tul").getText())};

        Element more = root.getChild("More");
        if (!Boolean.parseBoolean(more.getAttributeValue("active")))
            noneStandard[1] = null;
        else if (more.getChildren().size()==1)
            noneStandard[1] = new Double[]{Double.parseDouble(more.getChild("metrazh").getText())};
        else if (more.getChildren().size()==2)
            noneStandard[1] = new Double[]{Double.parseDouble(more.getChild("arz").getText())
                    , Double.parseDouble(more.getChild("tul").getText())};

        return noneStandard;
    }

    public String getLastPrint() {
        Element root = classElement.getChild("LabelType");
        if (root.getChild("LabelFileAdress") == null) {
            return "";
        }
        return root.getChild("LabelFileAdress").getText();
    }

    public String getLastTitle() {
        Element root = classElement.getChild("LabelType");
        if (root.getChild("LabelTitle") == null) {
            return "";
        }
        return root.getChild("LabelTitle").getText();
    }
}
